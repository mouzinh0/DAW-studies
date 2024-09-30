import jakarta.servlet.*
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*
// SLF4J logging
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@SpringBootApplication
class DemoApplication


fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

// Initialize the logger
private val logger: Logger = LoggerFactory.getLogger(LocaleArgumentResolver::class.java)

// First simple implementation
/*@RestController
class HelloController {
	@GetMapping("/hello")
	fun greet(): String {
		return "Hello, world!"
	}
}*/

// Without last implementation
/*@RestController
@RequestMapping("/hello")
class HelloController(val greetingService: GreetingService) {
// Handler (GET)
	@GetMapping
	fun greet(@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "en") language: String): ResponseEntity<String> {
		val greeting = greetingService.greet(language)
		return ResponseEntity.ok()  // 200
			.header(HttpHeaders.CONTENT_LANGUAGE, language)
			.body(greeting)
	}
}*/

// curl -X GET http://localhost:8080/hello
// Controller -> Dependency in greetingService via constructor injection
// Don't need @Autowired val greetingService because is one constructor
@RestController
@RequestMapping("/hello")
class HelloController(val greetingService: GreetingService) {
	// Handler (GET)
	@GetMapping
	fun greet(locale: Locale): ResponseEntity<String> {
		val greeting = greetingService.greet(locale.language)
		return ResponseEntity.ok()	// 200
			.header(HttpHeaders.CONTENT_LANGUAGE, locale.language)
			.body(greeting)
	}
}



// curl -H "Accept-Language: es" http://localhost:8080/hello
@Service
class GreetingService {
	fun greet(language: String): String {
		return when (language) {
			"pt" -> "Ola, mundo"
			"es" -> "Hola, mundo!"
			"fr" -> "Bonjour, le monde!"
			else -> "Hello, world!"
		}
	}
}

/**
 * A servlet filter contributes to the handling of HTTP responses, by using [HttpServletRequest]
 * and eventually mutating [HttpServletResponse] *before* and *after* the request is handled by a server.
 * Multiple filters are organized in a pipeline.
 */

@Component
class RequestLoggingFilter : HttpFilter() {

	override fun doFilter(request:  HttpServletRequest,
						  response: HttpServletResponse,
						  chain: FilterChain) {


		val startTime = System.currentTimeMillis()

		chain.doFilter(request, response)

		val duration = System.currentTimeMillis() - startTime
		val logMessage = "Method: ${request.method}, URI: ${request.requestURI}, Status: ${response.status}, Duration: ${duration}ms"

		println(logMessage)
	}

}

// Handler Interceptor to set Controller information and log it
// Executed before the request reaches the controller
@Component
class LoggingInterceptor : HandlerInterceptor {
	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		if (handler is HandlerMethod) {
			addToAttributes(handler, request)
		}
		return true
	}

	companion object {

		 fun addToAttributes(handler: HandlerMethod, request: HttpServletRequest) {
			 request.setAttribute("controllerName", handler.beanType.simpleName)
			 request.setAttribute("handlerMethodName", handler.method.name)  // Kotlin reflect is needed for this

			 val controllerName = request.getAttribute("controllerName")
			 val methodName = request.getAttribute("handlerMethodName")

			 println("Controller: $controllerName, Method: $methodName")

		 }
	}
}

// Registers Interceptor
/*@Configuration
class WebConfig(private val loggingInterceptor: LoggingInterceptor) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(loggingInterceptor)
	}
}*/

@Configuration
class WebConfig(
	private val loggingInterceptor: LoggingInterceptor
) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		logger.info("Adding InterceptorRegistry to the list")
		registry.addInterceptor(loggingInterceptor)
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		logger.info("Adding LocaleArgumentResolver to the list")
		resolvers.add(LocaleArgumentResolver())
	}
}




/* 	HandlerMethodArgumentResolver is a Spring interface that provides
 *	a way to customize how specific types of controller method arguments (parameters)
 *	are resolved from the HTTP request. */

@Component
class LocaleArgumentResolver : HandlerMethodArgumentResolver {


	// Ensures that this argument resolver only works for controller method parameters
	// that are expecting a Locale object, not String, Int etc
	override fun supportsParameter(parameter: MethodParameter): Boolean {
		logger.info("Checking parameter: {}", parameter.parameterType)
		// Check if the parameter is of type Locale
		return parameter.parameterType == Locale::class.java
	}

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any? {
		val languageTag = webRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE) ?: "en"
		logger.info("Resolved language tag: {}", languageTag)
		return Locale.forLanguageTag(languageTag)
	}
}
