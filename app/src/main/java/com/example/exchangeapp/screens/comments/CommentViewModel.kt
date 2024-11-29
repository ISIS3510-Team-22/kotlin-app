package com.example.exchangeapp.screens.comments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.model.service.impl.CommentService
import com.example.exchangeapp.model.service.module.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.exchangeapp.model.service.UserRepository

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentService: CommentService,
    private val userRepository: UserRepository,
    private val accountService: AccountService
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    val currentUserId = accountService.currentUserId

    fun addComment(comment: Comment, onComplete: (Boolean) -> Unit) {
        commentService.addComment(comment) {
            onComplete(it)
        }
    }

    fun fetchUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser(currentUserId).collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun fetchComments(university: String) {
        viewModelScope.launch {
            commentService.getCommentsByUniversity(university)
                .collect { comments ->
                    _comments.value = comments
                    Log.d("COMMENTS", comments.toString() + "1")
                }
        }
    }

    fun likeComment(comment: Comment, currentLikes: Int) {
        val commentId = comment.id
        val newLikes = currentLikes + 1
        commentService.updateLikes(commentId, newLikes) { success ->
            if (success) {
                _comments.value = _comments.value.map {
                    if (it.id == commentId) it.copy(likes = newLikes) else it
                }
            }
        }
    }
}