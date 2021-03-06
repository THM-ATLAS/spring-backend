package com.example.atlasbackend.exception


/***** [4xx] CLIENT ERRORS *****/



/** [400] BAD REQUEST **/

// One/multiple requested/posted parameters are empty
object EmptyParameterException : RuntimeException()

// One/multiple requested/posted parameters are invalid types
object InvalidParameterTypeException : RuntimeException()

// One/multiple requested/posted parameters are too long/short
object InvalidParameterLengthException : RuntimeException()

// Invalid Exercise ID when creating exercise
object InvalidExerciseIDException : RuntimeException()

// Invalid Module ID when creating module
object InvalidModuleIDException : RuntimeException()

//Invalid Submission Type given for an exercise
object InvalidSubmissionTypeIDException : RuntimeException()

//Invalid Question ID when creating question
object InvalidQuestionIDException : RuntimeException()

//Invalid Answer ID when creating question
object InvalidAnswerIDException : RuntimeException()

// Invalid User ID when creating user
object InvalidUserIDException : RuntimeException()

// Invalid Submission ID when creating submission
object InvalidSubmissionIDException : RuntimeException()

// Invalid Rating ID when creating rating
object InvalidRatingIDException : RuntimeException()

// Invalid Tag ID when creating tag
object InvalidTagIDException : RuntimeException()

// Invalid Role ID when assigning role
object InvalidRoleIDException: RuntimeException()

// Invalid Notification ID when creating notification
object  InvalidNotificationIDException : RuntimeException()

// Invalid Asset ID when creating Asset
object InvalidAssetIDException : RuntimeException()

// Invalid Icon ID when creating Icon
object InvalidIconIDException : RuntimeException()

// Icon is in use and can't be deleted
object IconInUseException : RuntimeException()

// Invalid Referral ID when creating Referral
object InvalidReferralIDException : RuntimeException()

/** [401] UNAUTHORIZED **/

// Invalid Credentials when trying to log in
object InvalidCredentialsException : RuntimeException()


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

// User is not allowed to edit these settings
object NoPermissionToModifySettingsException : RuntimeException()

// User is not allowed to create/edit/delete tags
object NoPermissionToModifyTagsException : RuntimeException()

// User is not allowed to assign/remove tags for this exercise
object NoPermissionToModifyExerciseTagsException : RuntimeException()

// User is not allowed to assign/remove roles of a user
object NoPermissionToModifyUserRolesException : RuntimeException()

// User is not allowed to add/remove multiple users at once
object NoPermissionToModifyMultipleUsersException : RuntimeException()

// User is not allowed to modify admins (no one is)
object NoPermissionToModifyAdminException : RuntimeException()

// User is not allowed to add this user to module (needs to be self/admin/teacher)
object NoPermissionToAddUserToModuleException: RuntimeException()

// User is not allowed to remove this user from module (needs to be self/admin/teacher)
object NoPermissionToRemoveUserFromModuleException: RuntimeException()

// User is not allowed to be added to modules
object UserCannotBeAddedToModuleException: RuntimeException()

// User can't be found in module
object UserNotInModuleException: RuntimeException()

// Submission was too late
object SubmissionAfterDeadlineException : RuntimeException()

// User doesn't have access to exercise (Not in module & exercise private)
object NoAccessToExerciseException : RuntimeException()

// User didn't enter Submission for this Exercise, can't rate exercise
object UserNeedsToSubmitBeforeRatingException : RuntimeException()

// User is not allowed to Post this Notification
object NoPermissionToPostNotificationException : RuntimeException()

// User is not allowed to delete this Notification relation to a User
object NoPermissionToRemoveNotificationRelationException : RuntimeException()

// User is not allowed to delete this Notification
object NoPermissionToDeleteNotificationException : RuntimeException()

// User is not allowed to add a Tag to this Module
object NoPermissionToModifyModuleTagException : RuntimeException()

// User is not allowed to create this Icon
object NoPermissionToCreateIconException : RuntimeException()

// User is not allowed to delete this icon
object  NoPermissionToDeleteIconException : RuntimeException()

// User is not permitted to mark this notification as read
object  NoPermissionToMarkAsReadException : RuntimeException()

// User is not permitted to modify this referral
object  NoPermissionToModifyReferralsException : RuntimeException()

/** [404] NOT FOUND **/

// Accessed Page doesn't exist
object PageNotFoundException : RuntimeException()

object LanguageNotFoundException: RuntimeException()

// Exercise ID doesn't exist
object ExerciseNotFoundException : RuntimeException()

// Module ID doesn't exist
object ModuleNotFoundException : RuntimeException()

// Question ID doesn't exist
object QuestionNotFoundException : RuntimeException()

// Answer ID doesn't exist
object AnswerNotFoundException : RuntimeException()

// User ID doesn't exist
object UserNotFoundException : RuntimeException()

// Submission ID doesn't exist
object SubmissionNotFoundException : RuntimeException()

// Rating ID doesn't exist
object RatingNotFoundException : RuntimeException()

// Tag ID doesn't exist
object TagNotFoundException : RuntimeException()

// Role ID doesn't exist
object RoleNotFoundException : RuntimeException()

// Setting ID doesn't exist
object SettingNotFoundException : RuntimeException()

// Notification ID doesn't exist
object NotificationNotFoundException : RuntimeException()

// Asset ID doesn't exist
object AssetNotFoundException : RuntimeException()

// Icon ID doesn't exist
object IconNotFoundException : RuntimeException()

// Referral ID doesn't exist
object ReferralNotFoundException : RuntimeException()

/** [422] UNPROCESSABLE ENTITY **/

// Entity couldn't be processed
object UnprocessableEntityException : RuntimeException()

// Username already exists in the database
object UserAlreadyExistsException : RuntimeException()

// User already submitted to that exercise
object SubmissionAlreadyExistsException: RuntimeException()

object WrongSubmissionTypeException: RuntimeException()

object QuestionDoesNotBelongToExerciseException: RuntimeException()

object AnswerDoesNotBelongToQuestionException: RuntimeException()

object ExerciseMustIncludeMcSchemeException: RuntimeException()
// Username is reserved for LDAP users
object ReservedLdapUsernameException : RuntimeException()

// Password does not meet the required criteria
object BadPasswordException : RuntimeException()



/***** [5xx] SERVER ERRORS *****/



/** [500] INTERNAL SERVER ERROR **/

// Unexpected Server Errors
object InternalServerError : RuntimeException()


/** [501] NOT IMPLEMENTED **/

// Method not implemented yet
object NotYetImplementedException : RuntimeException()
