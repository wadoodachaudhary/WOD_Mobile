package com.jtools.javawebutils.ioutils;

import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class DiskFileUpload extends FileUploadBase {

	// ----------------------------------------------------------- Data members

	/**
	 * The factory to use to create new form items.
	 */
	private DefaultFileItemFactory fileItemFactory;

	// ----------------------------------------------------------- Constructors

	/**
	 * Constructs an instance of this class which uses the default factory to
	 * create <code>FileItem</code> instances.
	 * 
	 * @see #DiskFileUpload(DefaultFileItemFactory fileItemFactory)
	 */
	public DiskFileUpload() {
		super();
		this.fileItemFactory = new DefaultFileItemFactory();
	}

	/**
	 * Constructs an instance of this class which uses the supplied factory to
	 * create <code>FileItem</code> instances.
	 * 
	 * @see #DiskFileUpload()
	 */
	public DiskFileUpload(DefaultFileItemFactory fileItemFactory) {
		super();
		this.fileItemFactory = fileItemFactory;
	}

	// ----------------------------------------------------- Property accessors

	/**
	 * Returns the factory class used when creating file items.
	 * 
	 * @return The factory class for new file items.
	 */
	public FileItemFactory getFileItemFactory() {
		return fileItemFactory;
	}

	/**
	 * Sets the factory class to use when creating file items. The factory must
	 * be an instance of <code>DefaultFileItemFactory</code> or a subclass
	 * thereof, or else a <code>ClassCastException</code> will be thrown.
	 * 
	 * @param factory
	 *            The factory class for new file items.
	 */
	public void setFileItemFactory(FileItemFactory factory) {
		this.fileItemFactory = (DefaultFileItemFactory) factory;
	}

	/**
	 * Returns the size threshold beyond which files are written directly to
	 * disk.
	 * 
	 * @return The size threshold, in bytes.
	 * 
	 * @see #setSizeThreshold(int)
	 */
	public int getSizeThreshold() {
		return fileItemFactory.getSizeThreshold();
	}

	/**
	 * Sets the size threshold beyond which files are written directly to disk.
	 * 
	 * @param sizeThreshold
	 *            The size threshold, in bytes.
	 * 
	 * @see #getSizeThreshold()
	 */
	public void setSizeThreshold(int sizeThreshold) {
		fileItemFactory.setSizeThreshold(sizeThreshold);
	}

	/**
	 * Returns the location used to temporarily store files that are larger than
	 * the configured size threshold.
	 * 
	 * @return The path to the temporary file location.
	 * 
	 * @see #setRepositoryPath(String)
	 */
	public String getRepositoryPath() {
		return fileItemFactory.getRepository().getPath();
	}

	/**
	 * Sets the location used to temporarily store files that are larger than
	 * the configured size threshold.
	 * 
	 * @param repositoryPath
	 *            The path to the temporary file location.
	 * 
	 * @see #getRepositoryPath()
	 */
	public void setRepositoryPath(String repositoryPath) {
		fileItemFactory.setRepository(new File(repositoryPath));
	}

	// --------------------------------------------------------- Public methods

	/**
	 * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
	 * compliant <code>multipart/form-data</code> stream. If files are stored
	 * on disk, the path is given by <code>getRepository()</code>.
	 * 
	 * @param req
	 *            The servlet request to be parsed. Must be non-null.
	 * @param sizeThreshold
	 *            The max size in bytes to be stored in memory.
	 * @param sizeMax
	 *            The maximum allowed upload size, in bytes.
	 * @param path
	 *            The location where the files should be stored.
	 * 
	 * @return A list of <code>FileItem</code> instances parsed from the
	 *         request, in the order that they were transmitted.
	 * 
	 * @exception FileUploadException
	 *                if there are problems reading/parsing the request or
	 *                storing files.
	 */
	public List /* FileItem */parseRequest(HttpServletRequest req, int sizeThreshold, long sizeMax, String path) throws FileUploadException {
		setSizeThreshold(sizeThreshold);
		setSizeMax(sizeMax);
		setRepositoryPath(path);
		return parseRequest(req);
	}

}
