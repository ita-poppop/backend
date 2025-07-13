package com.example.poppop.domain.comment.event;

import com.example.poppop.domain.comment.entity.Comment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReplyCreatedEvent extends ApplicationEvent {

    private final Comment reply;

    public ReplyCreatedEvent(Object source, Comment reply) {
        super(source);
        this.reply = reply;
    }
}
