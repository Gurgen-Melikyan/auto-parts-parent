package com.example.autopartsrest.service.impl;


import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.repository.CategoryRepository;
import com.example.autopartscommon.repository.CommentsRepository;
import com.example.autopartscommon.repository.ProductRepository;
import com.example.autopartsrest.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CategoryRepository categoryRepository;
    private final CommentsRepository commentsRepository;



    @Override
    public List<Comments> findAllComments() {
        return commentsRepository.findAll();
    }



    @Override
    public Comments findById(int id) {
        Optional<Comments> byId = commentsRepository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        return byId.get();
    }


    @Override
    public Comments save(Comments comments) {
        return commentsRepository.save(comments);
    }

    @Override
    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(int id) {
        return categoryRepository.existsById(id);
    }
}
