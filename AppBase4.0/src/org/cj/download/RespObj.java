package org.cj.download;

public class RespObj
{
	String	id;
	long	total;	   // 总大小
	long	downbytes; // 下载字节
	int	   progress;	// 进度
	String	name;
	int	   status;

	public RespObj(String id, int status)
	{
		// TODO Auto-generated constructor stub
		this.id = id;
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the total
	 */
	public long getTotal()
	{
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total)
	{
		this.total = total;
	}

	/**
	 * @return the downbytes
	 */
	public long getDownbytes()
	{
		return downbytes;
	}

	/**
	 * @param downbytes
	 *            the downbytes to set
	 */
	public void setDownbytes(long downbytes)
	{
		this.downbytes = downbytes;
	}

	/**
	 * @return the progress
	 */
	public int getProgress()
	{
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(int progress)
	{
		this.progress = progress;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}
}
