package com.simbirsoft.igorverbkin.realmtraining;

public enum CategoryHelp {
    HELPING_THINGS(R.string.help_with_things),
    BECOME_VOLUNTEER(R.string.volunteering_1),
    PROFESSIONAL_HELP(R.string.prof_help_2),
    HELP_MONEY(R.string.help_with_money);

    private final int descriptionAssistance;

    CategoryHelp(int descriptionAssistance) {
        this.descriptionAssistance = descriptionAssistance;
    }

    public int getDescriptionAssistance() {
        return descriptionAssistance;
    }
}
