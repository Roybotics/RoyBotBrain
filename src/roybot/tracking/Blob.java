package roybot.tracking;

public class Blob
{
	float gx, gy, rs, rn, re, rw, rsw, rse, rnw, rne,  r;

	public Blob(float[] pBlobInfo)
	{
		gx = pBlobInfo[0];
		gy = pBlobInfo[1];
		
		rs = pBlobInfo[2];
		rn = pBlobInfo[3];
		re = pBlobInfo[4];
		rw = pBlobInfo[5];
		
		rsw = pBlobInfo[6];
		rse = pBlobInfo[7];
		rnw = pBlobInfo[8];
		rne = pBlobInfo[9];
		r = getRadius(pBlobInfo);
	}

	public float getRadius()
	{
		return r;
	}


	@Override
	public String toString()
	{
		return String.format(	"Blob [gx=%s, gy=%s, rs=%s, rn=%s, re=%s, rw=%s, rsw=%s, rse=%s, rnw=%s, rne=%s, r=%s]",
													gx,
													gy,
													rs,
													rn,
													re,
													rw,
													rsw,
													rse,
													rnw,
													rne,
													r);
	}

	public static int getRadius(float[] pBlobInfo)
	{
		int r = 0;
		for(int i=2; i<=9; i++)
			r+=pBlobInfo[i];
		r = r/8;
		
		return r;
	}

}
