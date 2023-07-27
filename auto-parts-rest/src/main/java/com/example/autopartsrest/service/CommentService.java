package com.example.autopartsrest.service;

import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comments> findAllComments();
    Comments findById(int id);
    Comments save(Comments Comments);
    void deleteById(int id);

    boolean existsById(int id);
}
