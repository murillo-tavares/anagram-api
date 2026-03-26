package murillo.tavares.anagram_api.application.port.out;

import murillo.tavares.anagram_api.domain.model.Anagram;

public interface GenerateAnagramPort {

	Anagram generateAnagram();
}
