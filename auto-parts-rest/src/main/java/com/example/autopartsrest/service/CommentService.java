package com.example.autopartsrest.service;

import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.CreateCommentRequestDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comments> findAllComments();
    Comments createComment(CreateCommentRequestDto createCommentRequestDto,
                           User currentUser);
    Comments findById(int id) throws EntityNotFoundException;
    Comments save(Comments Comments);
    void deleteById(int id,User currentUser) throws EntityNotFoundException, UserUnauthorizedException;

    boolean existsById(int id);
}
