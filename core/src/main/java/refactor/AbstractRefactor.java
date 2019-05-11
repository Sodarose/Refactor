package refactor;

import lombok.Data;

@Data
public abstract class AbstractRefactor implements Refactor{
    private String refactorName;
    private String message;
    private String description;
    private String example;
}
