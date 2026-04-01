package murillo.tavares.anagram_api.adapter.in.web.renderer;

import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagramSolution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TextAnagramRenderer implements AnagramRenderer {

	@Override
	public MediaType contentType() {
		return MediaType.TEXT_PLAIN;
	}

	@Override
	public Object renderBody(DailyAnagram dailyAnagram) {
		return format(dailyAnagram);
	}

	public String format(DailyAnagram dailyAnagram) {
		int maxFormattedSolutionLength = dailyAnagram.solutions().stream()
				.map(solution -> solution.formattedValue().length())
				.max(Integer::compareTo)
				.orElse(0);

		String[] solutionLines = IntStream.range(0, dailyAnagram.solutions().size())
				.mapToObj(index -> formatSolution(index, dailyAnagram.solutions().get(index), maxFormattedSolutionLength))
				.toArray(String[]::new);
		int maxSolutionLineLength = IntStream.range(0, solutionLines.length)
				.map(index -> solutionLines[index].length())
				.max()
				.orElse(0);
		String lettersBox = formatLettersBox(dailyAnagram.letters(), maxSolutionLineLength);
		String lines = String.join(System.lineSeparator(), solutionLines);

		return String.join(
				System.lineSeparator(),
				lettersBox,
				"",
				lines
		);
	}

	private String formatLettersBox(String letters, int contentWidth) {
		String spacedLetters = letters.chars()
				.mapToObj(character -> Character.toString(Character.toUpperCase((char) character)))
				.collect(Collectors.joining(" "));
		String border = "+" + "-".repeat(spacedLetters.length() + 2) + "+";
		int leftPadding = Math.max(0, (contentWidth - border.length()) / 2);
		String indent = " ".repeat(leftPadding);

		return String.join(
				System.lineSeparator(),
				indent + border,
				indent + "| " + spacedLetters + " |",
				indent + border
		);
	}

	private String formatSolution(int index, DailyAnagramSolution solution, int maxValueLength) {
		if (solution.foundBy() == null) {
			return (index + 1) + ". " + solution.formattedValue();
		}

		String value = StringUtils.rightPad(solution.formattedValue(), maxValueLength);
		return (index + 1) + ". " + value + "  found by " + solution.foundBy();
	}
}
