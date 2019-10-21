package com.airesnor.wuxiacraft.cultivation.skills;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class SkillCap implements ISkillCap {

    private List<Skill> knownSkills;
    private Stack<BlockPos> toBreak;
    private float cooldown;
    private float castProgress;
    private List<Skill> SelectedSkills;
    private int ActiveSkillIndex;
    private boolean casting;
    private boolean doneCasting;

    public SkillCap() {
        this.knownSkills = new ArrayList<>();
        this.toBreak = new Stack<>();
        this.cooldown = 0;
        this.castProgress = 0;
        this.SelectedSkills = new ArrayList<>();
        this.ActiveSkillIndex = -1;
        this.casting = false;
        this.doneCasting = false;
    }

    @Override
    public List<Skill> getKnownSkills() {
        return this.knownSkills;
    }

    @Override
    public void addSkill(Skill skill) {
        this.knownSkills.add(skill);
    }

    @Override
    public void removeSkill(Skill skill) {
        this.knownSkills.remove(skill);
    }

    @Override
    public void addScheduledBlockBreaks(BlockPos pos) {
        this.toBreak.add(pos);
    }

    @Override
    public void addAllScheduledBlockBreaks(Stack<BlockPos> pos) {
        this.toBreak.addAll(pos);
    }

    @Override
    public BlockPos popScheduledBlockBreaks() {
        return this.toBreak.pop();
    }

    @Override
    public float getCooldown() {
        return this.cooldown;
    }

    @Override
    public void stepCooldown(float step) {
        this.cooldown+= step;
    }

    @Override
    public void resetCooldown() {
        this.cooldown = 0;
    }

    @Override
    public float getCastProgress() {
        return this.castProgress;
    }

    @Override
    public void stepCastProgress(float step) {
        this.castProgress+=step;
    }

    @Override
    public void resetCastProgress() {
        this.castProgress = 0;
    }

    @Override
    public boolean isScheduledEmpty() {
        return this.toBreak.isEmpty();
    }

    @Override
    public List<Skill> getSelectedSkills() {
        return this.SelectedSkills;
    }

    @Override
    public void addSelectedSkill(Skill skill) {
        this.SelectedSkills.add(skill);
    }

    @Override
    public void remSelectedSkill(Skill skill) {
        this.SelectedSkills.remove(skill);
    }

    @Override
    public int getActiveSkill() {
        return this.ActiveSkillIndex;
    }

    @Override
    public void setActiveSkill(int i) {
        this.ActiveSkillIndex = i;
    }

    @Override
    public boolean isCasting() {
        return this.casting;
    }

    @Override
    public void setCasting(boolean casting) {
        this.casting = casting;
    }

    @Override
    public boolean isDoneCasting() {
        return this.doneCasting;
    }

    @Override
    public void setDoneCasting(boolean doneCasting) {
        this.doneCasting = doneCasting;
    }
}
