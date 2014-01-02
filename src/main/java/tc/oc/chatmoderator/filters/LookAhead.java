package tc.oc.chatmoderator.filters;

import tc.oc.chatmoderator.words.CorrectedWord;
import tc.oc.chatmoderator.words.Word;
import tc.oc.chatmoderator.words.WordSet;

import java.util.List;

public interface LookAhead {
    public List<CorrectedWord> split(List<Word> components);

    public WordSet evaluate(List<CorrectedWord> correctedWords, WordSet wordSet);
}
