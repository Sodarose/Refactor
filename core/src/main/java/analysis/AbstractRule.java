package analysis;

import lombok.Data;

@Data
public abstract class AbstractRule implements Rule{
    private String ruleName;
    private String description;
    private String className;
    private boolean ruleStatus;
    private String message;
}
