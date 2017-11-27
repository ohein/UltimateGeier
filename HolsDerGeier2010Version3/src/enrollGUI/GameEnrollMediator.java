package enrollGUI;


public interface GameEnrollMediator {
	public void registerEnrolledPlayerList(EnrolledPlayerList list);
	public void registerAvailablePlayerList(AvailablePlayerList list);
	public void registerNetworkPlayerList(NetworkPlayerList list); // HOH
	public void registerEnrollButton(EnrollButton button);
	public void registerClearButton(ClearButton button);
	public void registerMillisPRField(MillisPRField field);
	public void registerEnrollCheckBox(EnrollCheckBox checkBox);
	public void registerCancelButton(CancelButton button);
	public void registerOKButton(OKButton button);
	public void registerRemoveButton(RemoveButton button);
	public void registerNumberOfRoundsField(NumberOfRoundsField field);
	public void registerSetPathButton(SetPathButton button);
	public void clearSelectionAvailablePlayerList();
	public void clearSelectionNetworkPlayerList();
	public void select();
	public void enroll();
	public void clear();
	public void millisPRFieldChanged();
	public void enrollCheckBoxStateChanged();
	public void cancel();
	public void ok();
	public void remove();
	public void numberOfRoundsFieldChanged();
	public void init();
	public void setPath();
	public void buzzardStateInvoked();
	public void cinderellaStateInvoked();
	public void dropStateInvoked();
}
