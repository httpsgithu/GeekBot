package be.hehehe.geekbot.web.server;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import be.hehehe.geekbot.annotations.GWTServlet;
import be.hehehe.geekbot.persistence.dao.QuizzDAO;
import be.hehehe.geekbot.persistence.dao.QuizzMergeDAO;
import be.hehehe.geekbot.persistence.model.QuizzMergeException;
import be.hehehe.geekbot.persistence.model.QuizzMergeRequest;
import be.hehehe.geekbot.persistence.model.QuizzPlayer;
import be.hehehe.geekbot.utils.BundleService;
import be.hehehe.geekbot.web.client.QuizzService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
@GWTServlet(path = "/Quizz/quizz")
public class QuizzServiceImpl extends RemoteServiceServlet implements
		QuizzService {

	@Inject
	QuizzDAO quizzDAO;

	@Inject
	QuizzMergeDAO quizzMergeDAO;

	@Inject
	BundleService bundleService;

	@Override
	public List<QuizzPlayer> getPlayers() {
		return quizzDAO.getPlayersOrderByPoints();

	}

	@Override
	public List<QuizzMergeRequest> getRequests() {
		return quizzMergeDAO.findAll();
	}

	@Override
	public void addMergeRequest(String player1, String player2)
			throws QuizzMergeException {
		quizzMergeDAO.add(player1, player2);
	}

	@Override
	public void acceptMergeRequest(String password, Long requestId) {
		String adminPassword = bundleService.getAdminPassword();
		if (StringUtils.equals(adminPassword, password)) {
			quizzMergeDAO.executeMerge(requestId);
		}

	}

	@Override
	public void denyMergeRequest(String password, Long requestId) {
		String adminPassword = bundleService.getAdminPassword();
		if (StringUtils.equals(adminPassword, password)) {
			quizzMergeDAO.deleteById(requestId);
		}
	}

}
