package imgToText;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class Main {
	
	public static void main(String[] args) {
		BufferedImage original = loadImage(Paths.get("/home/icaroerasmo/Downloads/bruna-marquezine-fa-450x363.jpeg"));
		original = resize(original,(int) (original.getWidth() * 0.3),(int) (original.getHeight() * 0.3));
		//BufferedImage blackAndWhite = convertToBlackAndWhite(original);
		BufferedImage blackAndWhite = convertToGrayScale(original);
		writeImg(blackAndWhite, Paths.get(System.getProperty("user.home"), "blackAndWhite.txt"));
		save(blackAndWhite, Paths.get(System.getProperty("user.home"), "blackAndWhite.png"));

	}
	
	private static BufferedImage loadImage(Path path) {
		try {
			return ImageIO.read(path.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static BufferedImage convertToBlackAndWhite(BufferedImage image) {
		BufferedImage blackAndWhiteImg = new BufferedImage(
				 image.getWidth(), image.getHeight(),
				 BufferedImage.TYPE_BYTE_BINARY);
	
	    Graphics2D graphics = blackAndWhiteImg.createGraphics();
	    graphics.drawImage(image, 0, 0, null);
	    graphics.dispose();
	    
	    return blackAndWhiteImg;
	}
	
	private static void writeImg(BufferedImage image, Path location) {
		
		FileWriter fw;
		try {
			fw = new FileWriter(Paths.get(System.getProperty("user.home"), "blackAndWhite.txt").toFile());
			                       
			BufferedWriter bw = new BufferedWriter(fw); 
			
			for (int i = 0; i <  image.getHeight(); i++) {
				
				StringBuilder builder = new StringBuilder();
				
	            for (int j = 0; j < image.getWidth(); j++) {
	            	
	            	if(image.getRGB(j, i) > -4194304) {
	            		builder.append(" ");
	            	} else if (image.getRGB(j, i) <= -4194304 && image.getRGB(j, i) > -6291456) {
	            		builder.append("I");
	            	} else if(image.getRGB(j, i) <= -6291456 && image.getRGB(j, i) > -8388608) {
	            		builder.append("/");
	            	} else {
	            		builder.append("$");
	            	}
	            }
	            
	            if (builder.toString().trim().isEmpty()) {
	                continue;
	            }
	            
	            System.out.println(builder.toString());
	            bw.write(builder.toString()+"\n");
	        }
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static BufferedImage resize(BufferedImage image, int width, int height) {

		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		
		return resizedImage;
	}
	
	private static BufferedImage convertToGrayScale(BufferedImage image){
		BufferedImage grayScale = new BufferedImage(image.getWidth(), image.getHeight(),  
			    BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = grayScale.getGraphics();  
		g.drawImage(image, 0, 0, null);  
		g.dispose();
		
		return grayScale;
	}
	
	private static void save(BufferedImage image, Path location){
		try {
			ImageIO.write(image, "png", location.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
