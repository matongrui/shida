package cn.hm.view;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cn.hm.bean.Book;
import cn.hm.bean.BookType;
import cn.hm.bean.Cart;
import cn.hm.bean.CartItem;
import cn.hm.bean.Order;
import cn.hm.service.BookService;
import cn.hm.service.BookTypeService;
import cn.hm.service.Impl.BookServiceImpl;
import cn.hm.service.Impl.BookTypeServiceImpl;

public class BookStore {

	private static BookService service = new BookServiceImpl();
	private static BookTypeService typeService = new BookTypeServiceImpl();
	private static Cart cart;
	
	public static void mune() {
		cart = new Cart();
		boolean flag = true;
		while (flag) {
			Scanner input = new Scanner(System.in);
			System.out.println("-------欢迎使用虹猫图书购物商城------");
			System.out.println("1.查询所有图书                 2.按名称查询图书");
			System.out.println("3.按类别查询图书             4.按作者查询图书");
			System.out.println("5.查询购物车信息             6.退出系统");
			System.out.println("请选择您要执行的操作:");
			String select = input.nextLine().trim();
			
			List<Book> books = null;
			switch (select) {
			case "1":
				System.out.println("图书编号\t图书名称\t图书作者\t图书价格\t图书类别");
                books = service.selectAll();
                print(books);
                addCart();
                showOrder();
				break;
			case "2":
				System.out.println("请输入您要查询的图书名称:");
				String bookName = input.next();
				books = service.selectByBookName(bookName);
				System.out.println("图书编号\t图书名称\t图书作者\t图书价格\t图书类别");
				print(books);
				addCart();
				showOrder();
				break;
			case "3":
				List<BookType> types = typeService.selectAllType();
				showType(types);
				System.out.println("请输入您要查询的类别编号:");
				int  typeID = input.nextInt();
				BookType type = typeService.selectByTypeID(typeID);
				if(type == null) {
					System.out.println("编号输入错误，类别不存在!");
					continue;
				}
				books = service.selectByTypeID(typeID);
				System.out.println("图书编号\t图书名称\t图书作者\t图书价格\t图书类别");
				print(books);
				addCart();
				showOrder();
				break;
			case "4":
				System.out.println("请输入您要查询图书的作者名称:");
				String author = input.next();
				books = service.selectByAuthor(author);
				System.out.println("图书编号\t图书名称\t图书作者\t图书价格\t图书类别");
				print(books);
				addCart();
				showOrder();
				break;
			case "5":
				showCart();
			case "6":
				System.out.println("欢迎您下次使用!");
				flag = false;
				break;
			default:
				System.out.println("无序选项，请重新输入!");
				break;

			}

		}
	}
	
	public static void print(List<Book> books) {
		for(int i = 0 ; i < books.size() ; i++) {
			Book book = books.get(i);
			System.out.println(book.getId()+"\t"+book.getName()+"\t"+
			book.getAuthor()+"\t"+book.getPrice()+"\t"+book.getType().getName());
		}
	}
	
	
	public static void showType(List<BookType> types) {
		for(int i = 0 ; i < types.size() ; i++) {
			BookType type = types.get(i);
			System.out.println(type.getId()+"\t"+type.getName());
		}
	}
	
	public static void addCart() {
		System.out.println("请输入图书编号：");
		Scanner input = new Scanner(System.in);
		int bid = input.nextInt();
		Book book = service.selectByBookID(bid);
		if(book==null) {
			System.out.println("不存在此图书编号");
			return;
		}
		System.out.println("请输入购买数量：");
		int count = input.nextInt();
		if(count<=0) {
			System.out.println("数量无效");
			return;
		}
		CartItem item = new CartItem();
		item.setBook(book);
		item.setCount(count);
		cart.add(item);
	}
	
	public static void showCart() {
		System.out.println("图书名称\t图书价格\t图书作者\t图书数量\t小计");
		Map<Integer,CartItem> items = cart.getMap();
		Collection<CartItem> lists = items.values();
		for(CartItem item:lists) {
			Book book = item.getBook();
			System.out.println(book.getName()+"\t"+book.getPrice()+"\t"+
			book.getAuthor()+"\t"+item.getCount()+"\t"+item.getSubtotal());
		}
		System.out.println("总价："+cart.total());
	}
	
	public static void showOrder() {
		Order ord = new Order();
		System.out.println("订单信息：");
		System.out.println("订单号："+ord.getOrderNumber());
		System.out.println("商品名称\t价格\t商品数量\t总计");
		Map<Integer,CartItem> items = cart.getMap();
		Collection<CartItem> lists = items.values();
		for(CartItem item:lists) {
			Book book = item.getBook();
			System.out.println(book.getName()+"\t"+book.getPrice()+"\t"+
			item.getCount()+"\t"+item.getSubtotal());
		}
	}
}
