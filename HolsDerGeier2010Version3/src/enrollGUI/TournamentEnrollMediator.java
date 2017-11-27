package enrollGUI;

public interface TournamentEnrollMediator extends GameEnrollMediator {
	
	public void registerTPlayersPerMatchField(TPlayersPerMatchField field);
	public void tPlayersPerMatchFieldChanged();

	//Eingefuegt von Gruppe 2
	public void registerFastSimulationCheckBox(FastSimulationCheckBox checkBox);
	public void fastSimulationCheckBoxStateChanged();
}
