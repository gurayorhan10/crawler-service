package com.project.crawlerservice.rabbit.data;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendMessage implements Serializable {
    private String to;
    private String title;
    private String content;
    private Boolean simple;
}
