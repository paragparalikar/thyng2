package com.thyng.model;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Lambda {
	
	@FunctionalInterface
	public interface Consumer_WithExceptions<T, E extends Exception> {
		void accept(T t) throws E;
	}

	@FunctionalInterface
	public interface BiConsumer_WithExceptions<T, U, E extends Exception> {
		void accept(T t, U u) throws E;
	}
	
	@FunctionalInterface
	public interface BiFunction_WithExceptions<T, U, R, E extends Exception> {
		R accept(T t, U u) throws E;
	}

	@FunctionalInterface
	public interface Function_WithExceptions<T, R, E extends Exception> {
		R apply(T t) throws E;
	}

	@FunctionalInterface
	public interface Supplier_WithExceptions<T, E extends Exception> {
		T get() throws E;
	}

	@FunctionalInterface
	public interface Runnable_WithExceptions<E extends Exception> {
		void run() throws E;
	}

	public static <T, E extends Exception> Consumer<T> consumer(Consumer_WithExceptions<T, E> consumer)
			throws E {
		return t -> {
			try {
				consumer.accept(t);
			} catch (Exception exception) {
				throwAsUnchecked(exception);
			}
		};
	}

	public static <T, U, E extends Exception> BiConsumer<T, U> uncheck(
			BiConsumer_WithExceptions<T, U, E> biConsumer) throws E {
		return (t, u) -> {
			try {
				biConsumer.accept(t, u);
			} catch (Exception exception) {
				throwAsUnchecked(exception);
			}
		};
	}

	public static <T, U, R, E extends Exception> BiFunction<T, U, R> uncheck(
			BiFunction_WithExceptions<T, U, R, E> biConsumer) throws E {
		return (t, u) -> {
			try {
				return biConsumer.accept(t, u);
			} catch (Exception exception) {
				throwAsUnchecked(exception);
				return null;
			}
		};
	}
	
	public static <T, R, E extends Exception> Function<T, R> function(Function_WithExceptions<T, R, E> function)
			throws E {
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception exception) {
				throwAsUnchecked(exception);
				return null;
			}
		};
	}

	public static <T, E extends Exception> Supplier<T> supplier(Supplier_WithExceptions<T, E> function)
			throws E {
		return () -> {
			try {
				return function.get();
			} catch (Exception exception) {
				throwAsUnchecked(exception);
				return null;
			}
		};
	}

	@SuppressWarnings("rawtypes")
	public static void uncheck(Runnable_WithExceptions t) {
		try {
			t.run();
		} catch (Exception exception) {
			throwAsUnchecked(exception);
		}
	}

	public static <R, E extends Exception> R uncheck(Supplier_WithExceptions<R, E> supplier) {
		try {
			return supplier.get();
		} catch (Exception exception) {
			throwAsUnchecked(exception);
			return null;
		}
	}

	public static <T, R, E extends Exception> R uncheck(Function_WithExceptions<T, R, E> function, T t) {
		try {
			return function.apply(t);
		} catch (Exception exception) {
			throwAsUnchecked(exception);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
		throw (E) exception;
	}

}
