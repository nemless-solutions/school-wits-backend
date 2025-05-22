package com.nemless.school_wits.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_result")
public class QuizResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ElementCollection
    @CollectionTable(name = "result_answers", joinColumns = @JoinColumn(name = "quiz_result_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "is_correct")
    private Map<Long, Boolean> answers = new HashMap<>();
}
