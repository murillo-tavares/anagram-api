package murillo.tavares.anagram_api.adapter.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
		name = "daily_anagram",
		uniqueConstraints = @UniqueConstraint(name = "uk_daily_anagram_puzzle_date", columnNames = "puzzle_date")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyAnagramEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "puzzle_date", nullable = false)
	private LocalDate puzzleDate;

	@Column(nullable = false)
	private String letters;

	@OneToMany(mappedBy = "dailyAnagram", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("answer ASC")
	private final List<DailyAnagramSolutionEntity> solutions = new ArrayList<>();

	public DailyAnagramEntity(LocalDate puzzleDate, String letters) {
		this.puzzleDate = puzzleDate;
		this.letters = letters;
	}

	public void addSolution(String answer, boolean found) {
		solutions.add(new DailyAnagramSolutionEntity(this, answer, found));
	}
}
