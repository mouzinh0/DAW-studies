package com.example.demo

import jakarta.servlet.*
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.parser.AcceptLanguage
import org.springframework.beans.factory.annotation.Autowired
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


@SpringBootApplication
class DemoApplication



fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

/*@RestController
class HelloController {
	@GetMapping("/hello")
	fun greet(): String {
		return "Hello, world!"vm
	}
}*/

// curl -X GET http://localhost:8080/hello

/*@RestController
@RequestMapping("/hello")
class HelloController(@Autowired val greetingService: GreetingService) {

	@GetMapping
	fun greet(@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "en") language: String): ResponseEntity<String> {
		val greeting = greetingService.greet(language)
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_LANGUAGE, language)
			.body(greeting)
	}
}*/

@RestController
@RequestMapping("/hello")
class HelloController(@Autowired val greetingService: GreetingService) {

	@GetMapping
	fun greet(locale: Locale): ResponseEntity<String> {
		val greeting = greetingService.greet(locale.language)
		return ResponseEntity.ok()
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

		//fun getFromAttributes(request: HttpServletRequest): HandlerMethod? = request.getAttribute(KEY) as? HandlerMethod
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
	private val loggingInterceptor: LoggingInterceptor,
	private val localeArgumentResolver: LocaleArgumentResolver
) : WebMvcConfigurer {

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(loggingInterceptor)
	}

	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(localeArgumentResolver)
	}
}




/* 	HandlerMethodArgumentResolver is a Spring interface that provides
 *	a way to customize how specific types of controller method arguments (parameters)
 *	are resolved from the HTTP request. */

@Component
class LocaleArgumentResolver : HandlerMethodArgumentResolver {

	override fun supportsParameter(parameter: MethodParameter): Boolean {
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
		return Locale.forLanguageTag(languageTag)
	}
}

