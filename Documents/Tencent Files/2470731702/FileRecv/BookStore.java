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
			System.out.println("-------��ӭʹ�ú�èͼ�鹺���̳�------");
			System.out.println("1.��ѯ����ͼ��                 2.�����Ʋ�ѯͼ��");
			System.out.println("3.������ѯͼ��             4.�����߲�ѯͼ��");
			System.out.println("5.��ѯ���ﳵ��Ϣ             6.�˳�ϵͳ");
			System.out.println("��ѡ����Ҫִ�еĲ���:");
			String select = input.nextLine().trim();
			
			List<Book> books = null;
			switch (select) {
			case "1":
				System.out.println("ͼ����\tͼ������\tͼ������\tͼ��۸�\tͼ�����");
                books = service.selectAll();
                print(books);
                addCart();
                showOrder();
				break;
			case "2":
				System.out.println("��������Ҫ��ѯ��ͼ������:");
				String bookName = input.next();
				books = service.selectByBookName(bookName);
				System.out.println("ͼ����\tͼ������\tͼ������\tͼ��۸�\tͼ�����");
				print(books);
				addCart();
				showOrder();
				break;
			case "3":
				List<BookType> types = typeService.selectAllType();
				showType(types);
				System.out.println("��������Ҫ��ѯ�������:");
				int  typeID = input.nextInt();
				BookType type = typeService.selectByTypeID(typeID);
				if(type == null) {
					System.out.println("������������𲻴���!");
					continue;
				}
				books = service.selectByTypeID(typeID);
				System.out.println("ͼ����\tͼ������\tͼ������\tͼ��۸�\tͼ�����");
				print(books);
				addCart();
				showOrder();
				break;
			case "4":
				System.out.println("��������Ҫ��ѯͼ�����������:");
				String author = input.next();
				books = service.selectByAuthor(author);
				System.out.println("ͼ����\tͼ������\tͼ������\tͼ��۸�\tͼ�����");
				print(books);
				addCart();
				showOrder();
				break;
			case "5":
				showCart();
			case "6":
				System.out.println("��ӭ���´�ʹ��!");
				flag = false;
				break;
			default:
				System.out.println("����ѡ�����������!");
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
		System.out.println("������ͼ���ţ�");
		Scanner input = new Scanner(System.in);
		int bid = input.nextInt();
		Book book = service.selectByBookID(bid);
		if(book==null) {
			System.out.println("�����ڴ�ͼ����");
			return;
		}
		System.out.println("�����빺��������");
		int count = input.nextInt();
		if(count<=0) {
			System.out.println("������Ч");
			return;
		}
		CartItem item = new CartItem();
		item.setBook(book);
		item.setCount(count);
		cart.add(item);
	}
	
	public static void showCart() {
		System.out.println("ͼ������\tͼ��۸�\tͼ������\tͼ������\tС��");
		Map<Integer,CartItem> items = cart.getMap();
		Collection<CartItem> lists = items.values();
		for(CartItem item:lists) {
			Book book = item.getBook();
			System.out.println(book.getName()+"\t"+book.getPrice()+"\t"+
			book.getAuthor()+"\t"+item.getCount()+"\t"+item.getSubtotal());
		}
		System.out.println("�ܼۣ�"+cart.total());
	}
	
	public static void showOrder() {
		Order ord = new Order();
		System.out.println("������Ϣ��");
		System.out.println("�����ţ�"+ord.getOrderNumber());
		System.out.println("��Ʒ����\t�۸�\t��Ʒ����\t�ܼ�");
		Map<Integer,CartItem> items = cart.getMap();
		Collection<CartItem> lists = items.values();
		for(CartItem item:lists) {
			Book book = item.getBook();
			System.out.println(book.getName()+"\t"+book.getPrice()+"\t"+
			item.getCount()+"\t"+item.getSubtotal());
		}
	}
}
