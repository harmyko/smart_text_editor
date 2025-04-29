package main.java.test;

import main.java.text_editor.*;

public class CloneTest {
     public static void main(String[] args) {

        System.out.println("*** Editor cloning demonstration! ***");

        SpellCheckEditor editor = new SpellCheckEditor();
        editor.addText("hello world");
        System.out.println("Original Editor:\t" + editor);

        SpellCheckEditor editorClone = null;
        try {
            editorClone = (SpellCheckEditor) editor.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("Cloned Editor:\t\t" + editorClone);

        System.out.println("** Adding \"new_word\" the original editor! **");
        editor.addText(" new_word");

        System.out.println("Original Editor:\t" + editor);
        System.out.println("Cloned Editor:\t\t" + editorClone);
        System.out.println();
    }

}
