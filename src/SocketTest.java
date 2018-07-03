import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketTest {

	public static void main(String[] args) throws IOException {

		Socket socket = new Socket("www.qssq666.cn", 80);// ip地址或者域名
															// 端口号，如果是https则是443

		// 接受数据的输入流
		final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// 发送数据 输出流
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		bw.write("GET /about/ HTTP/1.1");
		bw.write("\n");
		bw.write("Host: qssq666.cn");
		bw.write("\n");
		bw.write("Connection: Keep-Alive");
		bw.write("\n");
//		bw.write("\n");
		bw.flush();
		System.out.println("execute....");
		while (true) {
			String line = null;
			try {
				// System.out.println("========readLineStart=======");
				while ((line = br.readLine()) != null) {
					System.out.println("" + line);
				}
				// System.out.println("========readLineEnd=======");
			} catch (IOException e) {
				System.err.println("出现异常:" + e.toString());
				e.printStackTrace();
			}

			// System.out.println("========================");
		}

	}
}
