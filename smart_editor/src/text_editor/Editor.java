package text_editor;


public class Editor {

    private static final int MAX_TEXT_LENGTH = 1000;

    protected StringBuilder text;

    public Editor() {
        this.text = new StringBuilder();
    }

    public final void addChar(char character) {
        text.append(character);
    }

    public final void append(String string) {
        text.append(string);
    }

    public final void removeLastCharacter() {
        if (text.length() < MAX_TEXT_LENGTH) {
            text.deleteCharAt(text.length() - 1);
        }
    }
    
    public void removeWord() {
        if (text.length() > 0) {
            int lastSpaceIndex = text.lastIndexOf(" ");
            
            if (lastSpaceIndex == -1) {
                text.setLength(0);
            } else {
                text.delete(lastSpaceIndex, text.length());
            }
        }
    }
    
    public void removeWord(String word) {
        String textStr = text.toString();
        int index = textStr.lastIndexOf(word);

        while (index != -1) {
            boolean isWordBoundaryBefore = (index == 0 || Character.isWhitespace(textStr.charAt(index - 1)));
            boolean isWordBoundaryAfter = (index + word.length() == textStr.length() || Character.isWhitespace(textStr.charAt(index + word.length())));

            if (isWordBoundaryBefore && isWordBoundaryAfter) {
                text.delete(index, index + word.length());

                if (index > 0 && index < text.length() && text.charAt(index) == ' ') {
                    text.deleteCharAt(index);
                }
            }
            
            textStr = text.toString();
            index = textStr.lastIndexOf(word);
        }
    }


    public final int getTextLength() {
        return text.length();
    }

    public static Boolean isTextLengthValid(int length) {
        return length <= MAX_TEXT_LENGTH;
    }
    
    @Override
    public String toString() {
        return text.toString();
    }

}
