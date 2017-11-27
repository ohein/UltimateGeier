package enrollGUI;

public interface NetworkEnrollMediator extends GameEnrollMediator {
	public void registerPortField(PortField field);
	public void registerIpField(IpField field);
	public void IpFieldChanged();
	
}