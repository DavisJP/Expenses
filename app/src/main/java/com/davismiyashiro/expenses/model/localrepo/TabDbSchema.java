package com.davismiyashiro.expenses.model.localrepo;

/**
 * Class that defines the tables attributes
 */
public class TabDbSchema {
    public static final class TabTable {
        public static final String NAME = "tabs";

        /**
         *     private int groupId;
         *     private String groupName;
         *     private ArrayList<Participant> group;
         *     private ArrayList<Expense> mExpenses;
         */
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String GROUPNAME = "groupName";
        }
    }

    public static final class ParticipantTable {
        public static final String NAME = "participants";

        /**
         private String id;
         private String name;
         private String email;
         private int number;
         private String tabId;
         private ArrayList<Expense> expenses = new ArrayList<>();
         */
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String EMAIL = "email";
            public static final String NUMBER = "number";
            public static final String TAB_ID = "tab_id";
        }
    }

    public static final class ExpenseTable {
        public static final String NAME = "expenses";

        /**
         private String id;
         private String mDescription;
         private double mValue = 0;
         private ArrayList<Participant> mPayers = new ArrayList<>();
         */
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DESCRIPTION = "description";
            public static final String VALUE = "value";
            public static final String TAB_ID = "tab_id";
        }
    }



    public static final class SplitTable {
        public static final String NAME = "splits";

        /**
         private String id;
         private String participantId;
         private String expenseId;
         private int numberParticipants;
         private double valueByParticipant;
         */
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String PARTICIPANT_ID = "participantId";
            public static final String EXPENSE_ID = "expenseId";
            public static final String SPLIT_VAL = "valueByParticipant";
            public static final String TAB_ID = "tabId";
        }
    }
}
