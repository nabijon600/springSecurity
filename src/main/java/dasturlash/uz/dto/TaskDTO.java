package dasturlash.uz.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDTO {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdDate;

    public TaskDTO() {
    }

    public TaskDTO(String id, String title, String content, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
