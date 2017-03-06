package com.xolo.gzqc.bean.Db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 消费者搜索内容
 */
@Table(name = "searchcontent",onCreated = "CREATE UNIQUE INDEX realative_unique ON searchcontent(content)")
public class SearchContent {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "content",property = "NOT NULL")
   private String  content;

    public SearchContent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
