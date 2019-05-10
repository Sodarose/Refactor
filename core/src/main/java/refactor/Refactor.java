package refactor;


import model.Issue;
import model.ReCorrect;

public interface Refactor {
    ReCorrect refactor(Issue issue);
}
