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
    // TODO: Return what parameter(s) and how to fix (e.g. module teacher shouldn't be empty)
    @ExceptionHandler(value = [EmptyParameterException::class])
    fun exception(exception: EmptyParameterException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "EmptyParameterException", "One or multiple required parameters are empty.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // One/multiple requested/posted parameters are invalid types
    // TODO: Return what parameter(s) and how to fix (e.g. module id is int, but should be string)
    @ExceptionHandler(value = [InvalidParameterTypeException::class])
    fun exception(exception: InvalidParameterTypeException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidParameterTypeException", "One or multiple provided parameters had an invalid type.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // One/multiple requested/posted parameters are too long/short
    // TODO: Return what parameter(s) and how to fix (e.g. module name must be shorter than x)
    @ExceptionHandler(value = [InvalidParameterLengthException::class])
    fun exception(exception: InvalidParameterLengthException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidParameterLengthException", "One or multiple provided parameters had an invalid length.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid User ID when creating user
    @ExceptionHandler(value = [InvalidUserIDException::class])
    fun exception(exception: InvalidUserIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidUserIDException", "User ID must be zero when creating new user.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }


    /** [401] UNAUTHORIZED **/

    // Access Token is expired
    // TODO: Return how to authenticate, error & error description
    @ExceptionHandler(value = [TokenExpiredException::class])
    fun exception(exception: TokenExpiredException): ResponseEntity<ApiError> {
        val err = ApiError(401, HttpStatus.UNAUTHORIZED, "TokenExpiredException", "Your access token has expired.")
        return ResponseEntity<ApiError>(err, HttpStatus.UNAUTHORIZED)
    }

    // Access Token is missing
    // TODO: Return how to authenticate
    @ExceptionHandler(value = [TokenMissingException::class])
    fun exception(exception: TokenMissingException): ResponseEntity<ApiError> {
        val err = ApiError(401, HttpStatus.UNAUTHORIZED,"TokenMissingException", "You're not authorized to view this page.")
        return ResponseEntity<ApiError>(err, HttpStatus.UNAUTHORIZED)
    }


    /** [403] FORBIDDEN **/
    // User doesn't have the right to do this thing
    // TODO: Return needed privileges

    // User not allowed to view this page
    @ExceptionHandler(value = [AccessDeniedException::class])
    fun exception(exception: AccessDeniedException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "AccessDeniedException", "Insufficient permission to view this page.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this exercise
    @ExceptionHandler(value = [NoPermissionToDeleteExerciseException::class])
    fun exception(exception: NoPermissionToDeleteExerciseException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToDeleteExerciseException", "Insufficient permission to delete this exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this module
    @ExceptionHandler(value = [NoPermissionToDeleteModuleException::class])
    fun exception(exception: NoPermissionToDeleteModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN,"NoPermissionToDeleteModuleException", "Insufficient permission to delete this module.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this user
    @ExceptionHandler(value = [NoPermissionToDeleteUserException::class])
    fun exception(exception: NoPermissionToDeleteUserException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToDeleteUserException","Insufficient permission to delete this user.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this exercise
    @ExceptionHandler(value = [NoPermissionToEditExerciseException::class])
    fun exception(exception: NoPermissionToEditExerciseException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToEditExerciseException", "Insufficient permission to edit the information of this exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this module
    @ExceptionHandler(value = [NoPermissionToEditModuleException::class])
    fun exception(exception: NoPermissionToEditModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToEditModuleException", "Insufficient permission to edit the information of this module.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this user
    @ExceptionHandler(value = [NoPermissionToEditUserException::class])
    fun exception(exception: NoPermissionToEditUserException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToEditUserException", "Insufficient permission to edit the information of this user.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    /** [404] NOT FOUND **/

    // Accessed Page doesn't exist
    @ExceptionHandler(value = [PageNotFoundException::class])
    fun exception(exception: PageNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "PageNotFoundException", "Couldn't find requested page.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Exercise ID doesn't exist
    @ExceptionHandler(value = [ExerciseNotFoundException::class])
    fun exception(exception: ExerciseNotFoundException): ResponseEntity<ApiError> {
        // val errors: MutableList<String> = ArrayList()
        // errors.add("Exercise not found")
        val err = ApiError(404, HttpStatus.NOT_FOUND, "ExerciseNotFoundException", "Couldn't find requested exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // User ID doesn't exist
    @ExceptionHandler(value = [UserNotFoundException::class])
    fun exception(exception: UserNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "UserNotFoundException", "Couldn't find user.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Module ID doesn't exist
    @ExceptionHandler(value = [ModuleNotFoundException::class])
    fun exception(exception: ModuleNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "ModuleNotFoundException", "Couldn't find module.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Role ID doesn't exist
    @ExceptionHandler(value = [RoleNotFoundException::class])
    fun exception(exception: RoleNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "RoleNotFoundException", "Couldn't find role.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }



    /** [422] UNPROCESSABLE ENTITY **/

    // Entity couldn't be processed (Correct Type and Syntax)
    @ExceptionHandler(value = [UnprocessableEntityException::class])
    fun exception(exception: UnprocessableEntityException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "UnprocessableEntityException", "Not able to process one of the provided entities.")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }


    /***** [5xx] SERVER ERRORS *****/


    /** [500] INTERNAL SERVER ERROR **/

    // Unexpected Server Error
    @ExceptionHandler(value = [InternalServerError::class])
    fun exception(exception: InternalServerError): ResponseEntity<ApiError> {
        val err = ApiError(500, HttpStatus.INTERNAL_SERVER_ERROR)
        return ResponseEntity<ApiError>(err, HttpStatus.INTERNAL_SERVER_ERROR)
    }


    /** [501] NOT IMPLEMENTED **/

    // Method not implemented yet
    object NotImplementedException : RuntimeException()
    @ExceptionHandler(value = [NotYetImplementedException::class])
    fun exception(exception: NotYetImplementedException): ResponseEntity<ApiError> {
        val err = ApiError(501, HttpStatus.NOT_IMPLEMENTED, "NotYetImplementedException", "This function hasn't been implemented yet, please try again later.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_IMPLEMENTED)
    }

}
