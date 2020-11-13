package mil.pusdalops.persistence.settings.dao;

import java.util.List;

import mil.pusdalops.domain.settings.Settings;

public interface SettingsDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Settings findSettingsById(long id) throws Exception;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<Settings> findAllSettings() throws Exception;
	
	/**
	 * @param settings
	 * @return
	 * @throws Exception
	 */
	public Long save(Settings settings) throws Exception;
	
	/**
	 * @param settings
	 * @throws Exception
	 */
	public void update(Settings settings) throws Exception;
}
