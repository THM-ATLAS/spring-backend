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

    // Invalid Exercise ID when creating exercise
    @ExceptionHandler(value = [InvalidExerciseIDException::class])
    fun exception(exception: InvalidExerciseIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidExerciseIDException", "Exercise ID must be zero when creating new exercise, or has to be a multiple choice exercise when requesting Multiple Choice questions.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [InvalidSubmissionTypeIDException::class])
    fun exception(exception: InvalidSubmissionTypeIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidSubmissionTypeIDException", "Submission Type ID must be between 1 and 4")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Module ID when creating module
    @ExceptionHandler(value = [InvalidModuleIDException::class])
    fun exception(exception: InvalidModuleIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidModuleIDException", "Module ID must be zero when creating new module.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid User ID when creating user
    @ExceptionHandler(value = [InvalidUserIDException::class])
    fun exception(exception: InvalidUserIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidUserIDException", "User ID must be zero when creating new user.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Submission ID when creating submission
    @ExceptionHandler(value = [InvalidSubmissionIDException::class])
    fun exception(exception: InvalidSubmissionIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidSubmissionIDException", "Submission ID must be zero when creating new submission.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Rating ID when rating an exercise
    @ExceptionHandler(value = [InvalidRatingIDException::class])
    fun exception(exception: InvalidRatingIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidRatingIDException", "Rating ID must be zero when rating an exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Tag ID when creating tag
    @ExceptionHandler(value = [InvalidTagIDException::class])
    fun exception(exception: InvalidTagIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidTagIDException", "Tag ID must be zero when creating new tag.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Role ID when creating role
    @ExceptionHandler(value = [InvalidRoleIDException::class])
    fun exception(exception: InvalidRoleIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidRoleIDException", "Module Roles can only be: 2: student, 3: tutor, or 4: teacher, 3: tutor cannot be a global role")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Notification ID when creating notification
    @ExceptionHandler(value = [InvalidNotificationIDException::class])
    fun exception(exception: InvalidNotificationIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidNotificationIDException", "Notification ID must be zero when creating new notification")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }


    // Invalid Asset ID when creating asset
    @ExceptionHandler(value = [InvalidAssetIDException::class])
    fun exception(exception: InvalidAssetIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidAssetIDException", "Asset ID must be zero when creating a new asset.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }
    // Invalid Icon ID when creating Icon
    @ExceptionHandler(value = [InvalidIconIDException::class])
    fun exception(exception: InvalidIconIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidIconIDException", "icon ID must be zero when creating a new icon.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [InvalidQuestionIDException::class])
    fun exception(exception: InvalidQuestionIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidQuestionIDException", "Question ID must be zero when creating a new question.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [InvalidAnswerIDException::class])
    fun exception(exception: InvalidAnswerIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidAnswerIDException", "Answer ID must be zero when creating a new answer.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Icon is in use and can't be deleted
    @ExceptionHandler(value = [IconInUseException::class])
    fun exception(exception: IconInUseException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "IconInUseException", "icon can't be deleted until it isn't used anymore.")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    // Invalid Referral ID when creating Referral
    @ExceptionHandler(value = [InvalidReferralIDException::class])
    fun exception(exception: InvalidReferralIDException): ResponseEntity<ApiError> {
        val err = ApiError(400, HttpStatus.BAD_REQUEST, "InvalidReferralIDException", "Referral ID must be zero when creating a new referral of any kind")
        return ResponseEntity<ApiError>(err, HttpStatus.BAD_REQUEST)
    }

    /** [401] UNAUTHORIZED **/

    // Invalid Credentials when trying to log in
    @ExceptionHandler(value = [InvalidCredentialsException::class])
    fun exception(exception: InvalidCredentialsException): ResponseEntity<ApiError> {
        val err = ApiError(401, HttpStatus.UNAUTHORIZED,"InvalidCredentialsException", "The provided credentials did not match any known account")
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

    // User is not allowed to delete this submission
    @ExceptionHandler(value = [NoPermissionToDeleteSubmissionException::class])
    fun exception(exception: NoPermissionToDeleteSubmissionException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToDeleteSubmissionException","Insufficient permission to delete this submission.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this exercise rating
    @ExceptionHandler(value = [NoPermissionToDeleteRatingException::class])
    fun exception(exception: NoPermissionToDeleteRatingException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToDeleteRatingException","Insufficient permission to delete this exercise rating.")
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

    // User is not allowed to edit this submission
    @ExceptionHandler(value = [NoPermissionToEditSubmissionException::class])
    fun exception(exception: NoPermissionToEditSubmissionException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToEditSubmissionException", "Insufficient permission to edit requested submission.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit this exercise rating
    @ExceptionHandler(value = [NoPermissionToEditRatingException::class])
    fun exception(exception: NoPermissionToEditRatingException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToEditRatingException","Insufficient permission to edit this exercise rating.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to edit these settings
    @ExceptionHandler(value = [NoPermissionToModifySettingsException::class])
    fun exception(exception: NoPermissionToModifySettingsException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifySettingsException","Not allowed to edit the settings of another user.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to create/edit/delete tags
    @ExceptionHandler(value = [NoPermissionToModifyTagsException::class])
    fun exception(exception: NoPermissionToModifyTagsException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyTagsException", "Insufficient permission to modify list of tags.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to assign/remove tags for this exercise
    @ExceptionHandler(value = [NoPermissionToModifyExerciseTagsException::class])
    fun exception(exception: NoPermissionToModifyExerciseTagsException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyExerciseTagsException", "Insufficient permission to modify the tags of this exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to assign/remove roles of a user
    @ExceptionHandler(value = [NoPermissionToModifyUserRolesException::class])
    fun exception(exception: NoPermissionToModifyUserRolesException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyUserRolesException", "Insufficient permission to modify the roles of a user.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to add/remove multiple users at once
    @ExceptionHandler(value = [NoPermissionToModifyMultipleUsersException::class])
    fun exception(exception: NoPermissionToModifyMultipleUsersException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyMultipleUsersException", "Insufficient permission to modify multiple users at once.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to modify admins (no one is)
    @ExceptionHandler(value = [NoPermissionToModifyAdminException::class])
    fun exception(exception: NoPermissionToModifyAdminException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyAdminException", "Can't modify administrators.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to add this user to module (needs to be self/admin/teacher)
    @ExceptionHandler(value = [NoPermissionToAddUserToModuleException::class])
    fun exception(exception: NoPermissionToAddUserToModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToAddUserToModuleException", "Insufficient permission to add user to module.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to remove this user to module (needs to be self/admin/teacher)
    @ExceptionHandler(value = [NoPermissionToRemoveUserFromModuleException::class])
    fun exception(exception: NoPermissionToRemoveUserFromModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToRemoveUserFromModuleException", "Insufficient permission to remove user from module.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to be added to modules
    @ExceptionHandler(value = [UserCannotBeAddedToModuleException::class])
    fun exception(exception: UserCannotBeAddedToModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "UserCannotBeAddedToModuleException", "Guests cannot be part of a module")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User can't be found in module
    @ExceptionHandler(value = [UserNotInModuleException::class])
    fun exception(exception: UserNotInModuleException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "UserNotInModuleException", "Add the user to the module, before you can edit them.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // Submission was too late
    @ExceptionHandler(value = [SubmissionAfterDeadlineException::class])
    fun exception(exception: SubmissionAfterDeadlineException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "SubmissionAfterDeadlineException", "Time threshold for submission exceeded.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User doesn't have access to exercise (Not in module & exercise private)
    @ExceptionHandler(value = [NoAccessToExerciseException::class])
    fun exception(exception: NoAccessToExerciseException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoAccessToExerciseException", "User has to join module or exercise has to be public to submit.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User didn't enter Submission for this Exercise, can't rate exercise
    @ExceptionHandler(value = [UserNeedsToSubmitBeforeRatingException::class])
    fun exception(exception: UserNeedsToSubmitBeforeRatingException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "UserNeedsToSubmitBeforeRatingException", "User needs to submit first before rating an exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to Post this Notification
    @ExceptionHandler(value = [NoPermissionToPostNotificationException::class])
    fun exception(exception: NoPermissionToPostNotificationException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToPostNotificationException", "User has no Permission to Send this Notification")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this Notification relation to a User
    @ExceptionHandler(value = [NoPermissionToRemoveNotificationRelationException::class])
    fun exception(exception: NoPermissionToRemoveNotificationRelationException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToRemoveNotificationRelationException", "User has no Permission remove this Notification for this user")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this Notification
    @ExceptionHandler(value = [NoPermissionToDeleteNotificationException::class])
    fun exception(exception: NoPermissionToDeleteNotificationException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToRemoveNotificationException", "User has no Permission remove this Notification")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(value = [NoPermissionToModifyModuleTagException::class])
    fun exception(exception: NoPermissionToModifyModuleTagException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyModuleTagException", "User is not allowed to add a Tag to this Module")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to create this Icon
    @ExceptionHandler(value = [NoPermissionToCreateIconException::class])
    fun exception(exception: NoPermissionToCreateIconException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToCreateIconException", "Only Admins can create new Icons")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not allowed to delete this icon
    @ExceptionHandler(value = [NoPermissionToDeleteIconException::class])
    fun exception(exception: NoPermissionToDeleteIconException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToDeleteIconException", "Only Admins can delete new Icons")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not permitted to mark this notification as read
    @ExceptionHandler(value = [NoPermissionToMarkAsReadException::class])
    fun exception(exception: NoPermissionToMarkAsReadException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToMarkAsReadException", "Users may only mark their own notifications as read")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    // User is not permitted to modify this referral
    @ExceptionHandler(value = [NoPermissionToModifyReferralsException::class])
    fun exception(exception: NoPermissionToModifyReferralsException): ResponseEntity<ApiError> {
        val err = ApiError(403, HttpStatus.FORBIDDEN, "NoPermissionToModifyReferralsException", "User is not permitted to modify this referral")
        return ResponseEntity<ApiError>(err, HttpStatus.FORBIDDEN)
    }

    /** [404] NOT FOUND **/

    // Accessed Page doesn't exist
    @ExceptionHandler(value = [PageNotFoundException::class])
    fun exception(exception: PageNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "PageNotFoundException", "Couldn't find requested page.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [LanguageNotFoundException::class])
    fun exception(exception: LanguageNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "LanguageNotFoundException", "Couldn't find requested programming language.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Exercise ID doesn't exist
    @ExceptionHandler(value = [ExerciseNotFoundException::class])
    fun exception(exception: ExerciseNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "ExerciseNotFoundException", "Couldn't find requested exercise.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Module ID doesn't exist
    @ExceptionHandler(value = [ModuleNotFoundException::class])
    fun exception(exception: ModuleNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "ModuleNotFoundException", "Couldn't find requested module.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // User ID doesn't exist
    @ExceptionHandler(value = [UserNotFoundException::class])
    fun exception(exception: UserNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "UserNotFoundException", "Couldn't find requested user.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Submission ID doesn't exist
    @ExceptionHandler(value = [SubmissionNotFoundException::class])
    fun exception(exception: SubmissionNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "SubmissionNotFoundException", "Couldn't find requested submission.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Question ID doesn't exist
    @ExceptionHandler(value = [QuestionNotFoundException::class])
    fun exception(exception: QuestionNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "QuestionNotFoundException", "Couldn't find requested question.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Answer ID doesn't exist
    @ExceptionHandler(value = [AnswerNotFoundException::class])
    fun exception(exception: AnswerNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "AnswerNotFoundException", "Couldn't find requested answer.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Rating ID doesn't exist
    @ExceptionHandler(value = [RatingNotFoundException::class])
    fun exception(exception: RatingNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "RatingNotFoundException", "Couldn't find requested exercise rating.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Tag ID doesn't exist
    @ExceptionHandler(value = [TagNotFoundException::class])
    fun exception(exception: TagNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "TagNotFoundException", "Couldn't find requested tag.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Role ID doesn't exist
    @ExceptionHandler(value = [RoleNotFoundException::class])
    fun exception(exception: RoleNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "RoleNotFoundException", "Couldn't find requested role.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Setting ID doesn't exist
    @ExceptionHandler(value = [SettingNotFoundException::class])
    fun exception(exception: SettingNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "SettingNotFoundException", "Couldn't find requested setting.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Notification ID doesn't exist
    @ExceptionHandler(value = [NotificationNotFoundException::class])
    fun exception(exception: NotificationNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "NotificationNotFoundException", "Couldn't find requested notification.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Asset ID doesn't exist
    @ExceptionHandler(value = [AssetNotFoundException::class])
    fun exception(exception: AssetNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "AssetNotFoundException", "Couldn't find requested asset.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Icon ID doesn't exist
    @ExceptionHandler(value = [IconNotFoundException::class])
    fun exception(exception: IconNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "IconNotFoundException", "Couldn't find requested icon.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    // Referral ID doesn't exist
    @ExceptionHandler(value = [ReferralNotFoundException::class])
    fun exception(exception: ReferralNotFoundException): ResponseEntity<ApiError> {
        val err = ApiError(404, HttpStatus.NOT_FOUND, "ReferralNotFoundException", "Couldn't find requested referral.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_FOUND)
    }

    /** [422] UNPROCESSABLE ENTITY **/

    // Entity couldn't be processed (Correct Type and Syntax)
    @ExceptionHandler(value = [UnprocessableEntityException::class])
    fun exception(exception: UnprocessableEntityException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "UnprocessableEntityException", "Not able to process one of the provided entities.")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    // Username already exists in the database
    @ExceptionHandler(value = [UserAlreadyExistsException::class])
    fun exception(exception: UserAlreadyExistsException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "UnprocessableEntityException", "The provided username already exists. Edit the existing user or choose a different username.")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    // Submission with that user_id and that exercise_id already exists
    @ExceptionHandler(value = [SubmissionAlreadyExistsException::class])
    fun exception(exception: SubmissionAlreadyExistsException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "UnprocessableEntityException", "User already submitted to that task")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(value = [WrongSubmissionTypeException::class])
    fun exception(exception: WrongSubmissionTypeException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "WrongSubmissionTypeException", "Exercise Type and submission types have to match. The submission type given inside the submission and the actually given type have to match")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }
    // Username is reserved for LDAP users
    @ExceptionHandler(value = [ReservedLdapUsernameException::class])
    fun exception(exception: ReservedLdapUsernameException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "UnprocessableEntityException", "The submitted username is reserved for LDAP users")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    // Password does not meet the required criteria
    @ExceptionHandler(value = [BadPasswordException::class])
    fun exception(exception: BadPasswordException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "UnprocessableEntityException", "The submitted password does not meet the required criteria")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }


    @ExceptionHandler(value = [QuestionDoesNotBelongToExerciseException::class])
    fun exception(exception: QuestionDoesNotBelongToExerciseException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "QuestionDoesNotBelongToExerciseException", "Question given to a wrong task")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(value = [AnswerDoesNotBelongToQuestionException::class])
    fun exception(exception: AnswerDoesNotBelongToQuestionException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "AnswerDoesNotBelongToQuestionException", "Answer was given to a false question")
        return ResponseEntity<ApiError>(err, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(value = [ExerciseMustIncludeMcSchemeException::class])
    fun exception(exception: ExerciseMustIncludeMcSchemeException): ResponseEntity<ApiError> {
        val err = ApiError(422, HttpStatus.UNPROCESSABLE_ENTITY, "ExerciseMustIncludeMcSchemeException", "Exercises with mc type must include a mc scheme")
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
    @ExceptionHandler(value = [NotYetImplementedException::class])
    fun exception(exception: NotYetImplementedException): ResponseEntity<ApiError> {
        val err = ApiError(501, HttpStatus.NOT_IMPLEMENTED, "NotYetImplementedException", "This function hasn't been implemented yet, please try again later.")
        return ResponseEntity<ApiError>(err, HttpStatus.NOT_IMPLEMENTED)
    }

}
