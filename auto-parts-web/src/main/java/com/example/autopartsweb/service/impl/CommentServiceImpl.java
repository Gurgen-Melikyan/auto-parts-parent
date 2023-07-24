package com.example.autopartsweb.service.impl;
import com.example.autopartscommon.entity.Comments;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.CommentsRepository;
import com.example.autopartsweb.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentsRepository commentsRepository;


    @Override
    public List<Comments> findAllByProduct_Id(int product_id) {
        return commentsRepository.findAllByProduct_Id(product_id);
    }

    @Override
    public Comments save(User currentUser, Comments comments) {
        comments.setUser(currentUser);
        return commentsRepository.save(comments);
    }

    @Override
    public Comments findById(int id) {
        Optional<Comments> byId = commentsRepository.findById(id);
        if (byId.isEmpty()){
            return null;
        }
        return byId.get();
    }

    @Override
    public void deleteById(int id) {
        commentsRepository.deleteById(id);
    }
}
