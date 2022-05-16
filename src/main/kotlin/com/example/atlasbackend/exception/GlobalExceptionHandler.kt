package com.example.atlasbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    /***** [4xx] CLIENT ERRORS *****/


    /** [400] BAD REQUEST **/

    // One/multiple requested/posted parameters are empty
    object EmptyParameterException : RuntimeException()

    // One/multiple requested/posted parameters are invalid types
    object InvalidParameterTypeException : RuntimeException()

    // One/multiple requested/posted parameters are too long/short
    object InvalidParameterLengthException : RuntimeException()


    /** [401] UNAUTHORIZED **/

    // Access Token is expired (Return how to authenticate, error & error description)
    object TokenExpiredException : RuntimeException()

    // Access Token is missing (Only return how to authenticate)
    object TokenMissingException : RuntimeException()


    /** [403] FORBIDDEN **/
    // User doesn't have the right to do this thing
    // TODO: Return needed privileges

    // User not allowed to view this page
    @ExceptionHandler(value = [AccessDeniedException::class])
    fun exception(exception: AccessDeniedException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to view this page.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this exercise
    @ExceptionHandler(value = [NotAllowedToDeleteExerciseException::class])
    fun exception(exception: NotAllowedToDeleteExerciseException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to delete this exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this module
    @ExceptionHandler(value = [NotAllowedToDeleteModuleException::class])
    fun exception(exception: NotAllowedToDeleteModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to delete this module.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this user
    @ExceptionHandler(value = [NotAllowedToDeleteUserException::class])
    fun exception(exception: NotAllowedToDeleteUserException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to delete this user.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this exercise
    @ExceptionHandler(value = [NotAllowedToEditExerciseException::class])
    fun exception(exception: NotAllowedToEditExerciseException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to edit the information of this exercise")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this module
    @ExceptionHandler(value = [NotAllowedToEditModuleException::class])
    fun exception(exception: NotAllowedToEditModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to edit the information of this module")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this user
    @ExceptionHandler(value = [NotAllowedToEditUserException::class])
    fun exception(exception: NotAllowedToEditUserException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "Insufficient permission to edit the information of this user")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    /** [404] NOT FOUND **/

    // Accessed Page doesn't exist
    @ExceptionHandler(value = [PageNotFoundException::class])
    fun exception(exception: PageNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "The page you tried to access doesn't exist")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Exercise ID doesn't exist
    @ExceptionHandler(value = [ExerciseNotFoundException::class])
    fun exception(exception: ExerciseNotFoundException): ResponseEntity<ApiError> {
        // val errors: MutableList<String> = ArrayList()
        // errors.add("Exercise not found")
        val err = ApiError(404, HttpStatus.NOT_FOUND, "Couldn't find exercise")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // User ID doesn't exist
    @ExceptionHandler(value = [UserNotFoundException::class])
    fun exception(exception: UserNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "Couldn't find user")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Module ID doesn't exist
    @ExceptionHandler(value = [ModuleNotFoundException::class])
    fun exception(exception: ModuleNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "Couldn't find module")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    fun exception(exception: InternalServerErrorException): ResponseEntity<ApiError> {
        val err = ApiError(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
        return ResponseEntity<ApiError>(err, HttpStatus.INTERNAL_SERVER_ERROR)
    }


    /** [422] UNPROCESSABLE ENTITY **/

    // Entity couldn't be processed
    object UnprocessableEntityException : RuntimeException()


    /***** [5xx] SERVER ERRORS *****/


    /** [500] INTERNAL SERVER ERROR **/

    // Unexpected Server Errors
    object InternalServerErrorException : RuntimeException()


    /** [501] NOT IMPLEMENTED **/

    // Method not implemented yet
    object NotImplementedException : RuntimeException()


    /** [???] UNEXPECTED ERROR **/

    // Completely Unexpected
    object UnexpectedError : RuntimeException()

    // TODO: Add more errors

}
