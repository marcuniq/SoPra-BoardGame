package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DiceAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DieBean;

public class DiceArea extends AbstractArea {

    private Long id;
    private List<DieBean> rolledDice;

    public DiceArea(DiceAreaBean bean){
        this.id = bean.id();
        this.rolledDice = bean.rolledDice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DieBean> getRolledDice() {
        return rolledDice;
    }

    public void setRolledDice(List<DieBean> rolledDice) {
        this.rolledDice = rolledDice;
    }
}
