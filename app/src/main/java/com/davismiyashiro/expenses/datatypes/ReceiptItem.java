package com.davismiyashiro.expenses.datatypes;

/**
 * Object to encapsulate data for each item on the receipt
 */

public class ReceiptItem {
    private String participantId;
    private String participantName;
    private String expenseDesc;
    private double value;

    public ReceiptItem(String participantId, String participantName, String expenseDesc, double value) {
        this.participantId = participantId;
        this.participantName = participantName;
        this.expenseDesc = expenseDesc;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getParticipantId() {
        return participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptItem that = (ReceiptItem) o;

        if (Double.compare(that.value, value) != 0) return false;
        if (participantId != null ? !participantId.equals(that.participantId) : that.participantId != null)
            return false;
        if (participantName != null ? !participantName.equals(that.participantName) : that.participantName != null)
            return false;
        return expenseDesc != null ? expenseDesc.equals(that.expenseDesc) : that.expenseDesc == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = participantId != null ? participantId.hashCode() : 0;
        result = 31 * result + (participantName != null ? participantName.hashCode() : 0);
        result = 31 * result + (expenseDesc != null ? expenseDesc.hashCode() : 0);
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getExpenseDesc() {
        return expenseDesc;
    }
}
