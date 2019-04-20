
package com.w8x.web.model;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Commits {

    private String id;
    private String tree_id;
    private boolean distinct;
    private String message;
    private Date timestamp;
    private String url;
    private Author author;
    private Committer committer;
    private List<String> added;
    private List<String> removed;
    private List<String> modified;

}