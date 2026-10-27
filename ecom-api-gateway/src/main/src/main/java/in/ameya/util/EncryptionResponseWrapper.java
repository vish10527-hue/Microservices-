/*
 * package in.ameya.util;
 * 
 * import java.io.BufferedReader; import java.io.ByteArrayInputStream; import
 * java.io.IOException; import java.io.InputStream; import
 * java.io.InputStreamReader;
 * 
 * import jakarta.servlet.ReadListener; import
 * jakarta.servlet.ServletInputStream; import
 * jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletRequestWrapper; import
 * jakarta.servlet.http.HttpServletResponse; import
 * jakarta.servlet.http.HttpServletResponseWrapper;
 * 
 * public class EncryptionResponseWrapper extends HttpServletResponseWrapper {
 * 
 * private final String body;
 * 
 * public EncryptionResponseWrapper(HttpServletResponse response) throws
 * IOException { super(response);
 * 
 * StringBuilder stringBuilder = new StringBuilder(); BufferedReader
 * bufferedReader = null; try { InputStream inputStream = response.get; if
 * (inputStream != null) { bufferedReader = new BufferedReader(new
 * InputStreamReader(inputStream)); char[] charBuffer = new char[128]; int
 * bytesRead = -1; while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
 * stringBuilder.append(charBuffer, 0, bytesRead); }
 * 
 * } else { stringBuilder.append(""); } } catch (IOException ex) { throw ex; }
 * finally { if (bufferedReader != null) { try { bufferedReader.close(); } catch
 * (IOException ex) { throw ex; } } } String requestBody =
 * stringBuilder.toString();
 * System.out.println("EncryptionRequestWrapper.EncryptionRequestWrapper() : " +
 * requestBody); body = requestBody;
 * 
 * }
 * 
 * @Override public ServletInputStream getInputStream() throws IOException {
 * final ByteArrayInputStream byteArrayInputStream = new
 * ByteArrayInputStream(body.getBytes()); ServletInputStream servletInputStream
 * = new ServletInputStream() { public int read() throws IOException { return
 * byteArrayInputStream.read(); }
 * 
 * @Override public boolean isFinished() { return false; }
 * 
 * @Override public boolean isReady() { return false; }
 * 
 * @Override public void setReadListener(ReadListener listener) {
 * 
 * } }; return servletInputStream; }
 * 
 * @Override public BufferedReader getReader() throws IOException { return new
 * BufferedReader(new InputStreamReader(this.getInputStream())); } }
 */