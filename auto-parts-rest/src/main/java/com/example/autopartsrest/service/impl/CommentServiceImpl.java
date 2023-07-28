package com.example.autopartsrest.service.impl;


import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.Product;
import com.example.autopartscommon.entity.Role;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.CategoryRepository;
import com.example.autopartscommon.repository.CommentsRepository;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartsrest.dto.CreateCommentRequestDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import com.example.autopartsrest.mapper.CommentMapper;
import com.example.autopartsrest.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CategoryRepository categoryRepository;
    private final CommentsRepository commentsRepository;
    private final ProductRepository productRepository;
    private final CommentMapper commentMapper;


    @Override
    public List<Comments> findAllComments() {
        return commentsRepository.findAll();
    }

    @Override
    public Comments createComment(CreateCommentRequestDto createCommentRequestDto, User currentUser) {
        Optional<Product> byId = productRepository.findById(createCommentRequestDto.getProductId());
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        Comments comment = commentMapper.map(createCommentRequestDto);
        comment.setUser(currentUser);
        comment.setProduct(byId.get());
        commentsRepository.save(comment);
        return comment;
    }


    @Override
    public Comments findById(int id) throws EntityNotFoundException {
        Optional<Comments> byId = commentsRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("comment not found");
        }
        return byId.get();
    }


    @Override
    public Comments save(Comments comments) {
        return commentsRepository.save(comments);
    }

    @Override
    public void deleteById(int id, User currentUser) throws EntityNotFoundException, UserUnauthorizedException {
        Optional<Comments> byIdOptional = commentsRepository.findById(id);
        if (byIdOptional.isEmpty()) {
            throw new EntityNotFoundException("Comment with "+id+"not found");
        }
        Comments byId = byIdOptional.get();
        if (byId.getUser().getId() != currentUser.getId() && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new UserUnauthorizedException("you can change yours");
        }
        commentsRepository.deleteById(id);

    }


    @Override
    public boolean existsById(int id) {
        return categoryRepository.existsById(id);
    }
}
