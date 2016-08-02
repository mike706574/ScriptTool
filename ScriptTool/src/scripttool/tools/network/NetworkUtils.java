package scripttool.tools.network;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class NetworkUtils {

	public static ArrayList<String> getTemplateListFromRemote(String url, String username, String password, String extension) throws SocketException, IOException {
		final FTPClient ftp = new FTPClient();
		ftp.connect(url);
		ftp.login(username, password);

		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new IOException("Connection refused from host.  Reply code: " + reply);
		}

		ArrayList<String> templates = new ArrayList<String>();
		FTPFile[] files = ftp.listFiles();
		System.out.println("Num files: " + files.length);
		for (int i = 0; i < files.length; i++) {
			FTPFile file = files[i];
			if (file.isFile()) {
				String name = file.getName();
				String[] spl = name.split(".");
				if (spl.length > 0) {
					String ext = spl[spl.length];
					if (ext.equals(extension)) {
						templates.add(file.getName());
					}
				}
				
			}
		}
		return templates;
	}
}
