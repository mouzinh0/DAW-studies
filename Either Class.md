* The **Either** class is a sealed class designed to represent one of two possible outcomes: success or failure. It is often used as a functional programming alternative to exceptions for error handling, providing a more explicit and type-safe way to manage success and failure.

```
sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()

    data class Right<out R>(val value: R) : Either<Nothing, R>()
}

// Functions for when using Either to represent success or failure
fun <R> success(value: R) = Either.Right(value)

fun <L> failure(error: L) = Either.Left(error)

typealias Success<S> = Either.Right<S>
typealias Failure<F> = Either.Left<F>
```

* Either is used to encapsulate the result of a computation, where the computation could either:
  - Succeed and return a result, represented by Either.Right (a success case).
  - Fail and return an error, represented by Either.Left (a failure case).
 
  * Left: This class represents the failure case. It holds a value of type L (the failure type). This is indicated by the out L type parameter.

  * Right: This class represents the success case. It holds a value of type R (the success type).

* Helper Functions: success and failure

  These two utility functions make it easier to create instances of Either for success or failure:

    * fun <R> success(value: R): Either.Right<R>:

This is a helper function that simplifies the creation of a Right (success) instance.
> Example: success(123) will return Either.Right(123).

  * fun <L> failure(error: L): Either.Left<L>:

This is a helper function that simplifies the creation of a Left (failure) instance.
> Example: failure("Error") will return Either.Left("Error").
