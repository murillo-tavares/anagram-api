package murillo.tavares.anagram_api.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
		name = "daily_anagram_solution",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_daily_anagram_solution_answer",
				columnNames = {"daily_anagram_id", "answer"}
		)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyAnagramSolutionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "daily_anagram_id", nullable = false)
	private DailyAnagramEntity dailyAnagram;

	@Column(nullable = false)
	private String answer;

	@Column(nullable = false)
	private boolean found;

	public DailyAnagramSolutionEntity(DailyAnagramEntity dailyAnagram, String answer, boolean found) {
		this.dailyAnagram = dailyAnagram;
		this.answer = answer;
		this.found = found;
	}

	public void markAsFound() {
		this.found = true;
	}

}
