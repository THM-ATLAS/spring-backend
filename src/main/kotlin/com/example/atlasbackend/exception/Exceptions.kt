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

// Invalid User ID when creating user
object InvalidUserIDException : RuntimeException()

// Invalid Module ID when creating module
object InvalidModuleIDException : RuntimeException()

// Invalid Role ID when creating role
object InvalidRoleIDException: RuntimeException()

// Invalid Tag ID when creating tag
object InvalidTagIDException : RuntimeException()

// Invalid Submission ID when creating submission
object InvalidSubmissionIDException : RuntimeException()

// Invalid Rating ID when creating rating
object InvalidRatingIDException : RuntimeException()

// Invalid User ID, User is not in Module
object UserNotInModuleException: RuntimeException()


/** [401] UNAUTHORIZED **/

// Access Token is expired (Return how to authenticate, error & error description)
object TokenExpiredException : RuntimeException()

// Access Token is missing (Only return how to authenticate)
object TokenMissingException : RuntimeException()

// Error during token creation
object TokenCreationError : RuntimeException()


/** [403] FORBIDDEN **/
// User doesn't have the right to do this thing (Return needed privileges)

// User not allowed to view this page
object AccessDeniedException : RuntimeException()

// User is not allowed to delete this exercise
object NoPermissionToDeleteExerciseException : RuntimeException()

// User is not allowed to delete this module
object NoPermissionToDeleteModuleException : RuntimeException()

// User is not allowed to delete this user
object NoPermissionToDeleteUserException : RuntimeException()

// User is not allowed to delete this submission
object NoPermissionToDeleteSubmissionException : RuntimeException()

// User is not allowed to delete this exercise rating
object NoPermissionToDeleteRatingException : RuntimeException()

// User is not allowed to edit this exercise
object NoPermissionToEditExerciseException : RuntimeException()

// User is not allowed to edit this module
object NoPermissionToEditModuleException : RuntimeException()

// User is not allowed to edit this user
object NoPermissionToEditUserException : RuntimeException()

// User is not allowed to edit this submission
object NoPermissionToEditSubmissionException : RuntimeException()

// User is not allowed to edit this exercise rating
object NoPermissionToEditRatingException : RuntimeException()

// User is not allowed to create/edit/delete tags
object NoPermissionToModifyTagsException : RuntimeException()

// User is not allowed to assign/remove tags for this exercise
object NoPermissionToModifyExerciseTagsException : RuntimeException()

// Submission was too late
object SubmissionAfterDeadlineException : RuntimeException()

// User cannot be added to module
object UserCannotBeAddedToModuleException: RuntimeException()


/** [404] NOT FOUND **/

// Accessed Page doesn't exist
object PageNotFoundException : RuntimeException()

// Exercise ID doesn't exist
object ExerciseNotFoundException : RuntimeException()

// User ID doesn't exist
object UserNotFoundException : RuntimeException()

// Module ID doesn't exist
object ModuleNotFoundException : RuntimeException()

// Role ID doesn't exist
object RoleNotFoundException : RuntimeException()

// Tag ID doesn't exist
object TagNotFoundException : RuntimeException()

// Submission ID doesn't exist
object SubmissionNotFoundException : RuntimeException()

// Rating ID doesn't exist
object RatingNotFoundException : RuntimeException()

// Setting ID doesn't exist
object SettingNotFoundException : RuntimeException()


/** [422] UNPROCESSABLE ENTITY **/

// Entity couldn't be processed
object UnprocessableEntityException : RuntimeException()



/***** [5xx] SERVER ERRORS *****/



/** [500] INTERNAL SERVER ERROR **/

// Unexpected Server Errors
object InternalServerError : RuntimeException()


/** [501] NOT IMPLEMENTED **/

// Method not implemented yet
object NotYetImplementedException : RuntimeException()
