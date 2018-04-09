package com.sd.smartlearningapplication.enums

enum class TypeOfQuestion(var questionLevel: Int) {
    HARD(3),
    MEDIUM(2),
    EASY(1);

    companion object {
        /**
         * Gets the Type of question from Question level
         */
        fun getTypeOfQuestion(questionLevel: Int): TypeOfQuestion {
            for (typeOfQuestions in values()) {
                if (questionLevel == typeOfQuestions.questionLevel) {
                    return typeOfQuestions
                }
            }
            throw IllegalArgumentException("No type of question found for $questionLevel")
        }
    }

}