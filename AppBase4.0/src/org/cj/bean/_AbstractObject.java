package org.cj.bean;

import java.io.Serializable;
import java.lang.reflect.Field;

public abstract class _AbstractObject extends Object implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	protected String	      id	             = "";

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

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		try
		{
			Class<? extends _AbstractObject> t = this.getClass();
			Field[] fields = t.getDeclaredFields();
			sb.append(this.getClass().getSimpleName());
			for ( int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				field.setAccessible(true);
				sb.append("\n" + field.getName());
				sb.append("：");
				if (field.getType() == Integer.class)
				{
					sb.append(field.getInt(this));
				} else if (field.getType() == Long.class)
				{
					sb.append(field.getLong(this));
				} else if (field.getType() == Boolean.class)
				{
					sb.append(field.getBoolean(this));
				} else if (field.getType() == char.class)
				{
					sb.append(field.getChar(this));
				} else if (field.getType() == Double.class)
				{
					sb.append(field.getDouble(this));
				} else if (field.getType() == Float.class)
				{
					sb.append(field.getFloat(this));
				} else
					sb.append(field.get(this));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		// TODO Auto-generated method stub
		return o instanceof _AbstractObject
		        && ((_AbstractObject) o).getId().equals(this.getId());
	}
}
