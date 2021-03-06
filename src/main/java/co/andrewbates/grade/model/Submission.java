package co.andrewbates.grade.model;

import java.util.UUID;

import co.andrewbates.grade.data.Info;
import co.andrewbates.grade.rubric.Score;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

@Info(path = "submissions")
public class Submission extends BaseModel {
    private static final long serialVersionUID = 1L;

    public enum Status {
        PASSED, FAILED, NOTGRADED,
    }

    private StringProperty studentName = new SimpleStringProperty();
    private ObservableMap<String, Score> scores = FXCollections.observableHashMap();
    private StringProperty grade = new SimpleStringProperty();

    private ObjectProperty<Status> status = new SimpleObjectProperty<>();

    private ObjectProperty<UUID> offeringID = new SimpleObjectProperty<>();

    private ObjectProperty<UUID> assignmentID = new SimpleObjectProperty<>();

    private int numScores;
    private int passedScores;

    public final StringProperty studentNameProperty() {
        return this.studentName;
    }

    public final String getStudentName() {
        return this.studentName.get();
    }

    public final void setStudentName(final String studentName) {
        this.studentName.set(studentName);
    }

    public void setStatus(Status status) {
        this.status.set(status);
        if (status == Status.NOTGRADED) {
            setGrade("");
        }
    }

    public Status getStatus() {
        return status.get();
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    public void setScore(Score score) {
        Score oldScore = scores.get(score.getName());
        if (oldScore != null) {
            numScores -= oldScore.getRange();
            passedScores -= oldScore.getScore();
        }
        scores.put(score.getName(), score);

        numScores += score.getRange();
        passedScores += score.getScore();

        setGrade("" + passedScores + "/" + numScores);

        if (passedScores == numScores) {
            setStatus(Status.PASSED);
        } else {
            setStatus(Status.FAILED);
        }
    }

    public ObservableMap<String, Score> getScores() {
        return scores;
    }

    public final StringProperty gradeProperty() {
        return this.grade;
    }

    public final String getGrade() {
        return this.gradeProperty().get();
    }

    public final void setGrade(final String grade) {
        this.gradeProperty().set(grade);
    }

    public String getLog() {
        StringBuilder builder = new StringBuilder();
        for (Score score : getScores().values()) {
            builder.append(score.getMessage());
            builder.append("\n");
        }
        return builder.toString();
    }

    public final ObjectProperty<UUID> assignmentIDProperty() {
        return this.assignmentID;
    }

    public final UUID getAssignmentID() {
        return this.assignmentIDProperty().get();
    }

    public final void setAssignmentID(final UUID assignmentID) {
        this.assignmentIDProperty().set(assignmentID);
    }

    public final ObjectProperty<UUID> offeringIDProperty() {
        return this.offeringID;
    }

    public final UUID getOfferingID() {
        return this.offeringIDProperty().get();
    }

    public final void setOfferingID(final UUID offeringID) {
        this.offeringIDProperty().set(offeringID);
    }

}
