package com.example.atlasbackend.exception

// TODO: Try to combine similar functions


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
// User doesn't have the right to do this thing (Return needed privileges)

// User not allowed to view this page
object AccessDeniedException : RuntimeException()

// User is not allowed to delete this exercise
object NotAllowedToDeleteExerciseException : RuntimeException()

// User is not allowed to delete this module
object NotAllowedToDeleteModuleException : RuntimeException()

// User is not allowed to delete this user
object NotAllowedToDeleteUserException : RuntimeException()

// User is not allowed to edit this exercise
object NotAllowedToEditExerciseException : RuntimeException()

// User is not allowed to edit this module
object NotAllowedToEditModuleException : RuntimeException()

// User is not allowed to edit this user
object NotAllowedToEditUserException : RuntimeException()


/** [404] NOT FOUND **/

// Accessed Page doesn't exist
object PageNotFoundException : RuntimeException()

// Exercise ID doesn't exist
object ExerciseNotFoundException : RuntimeException()

// User ID doesn't exist
object UserNotFoundException : RuntimeException()

// Module ID doesn't exist
object ModuleNotFoundException : RuntimeException()


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
