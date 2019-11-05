# JSR303/JSR-349
web开发中各种参数校验的逻辑很繁琐，为了减轻开发者的负担，Java发布了 JSR303/JSR-349数据校验规范。

> JSR303 是一项标准，JSR-349 是其的升级版本，添加了一些新特性，他们规定一些校验规范即校验注解，如 @Null，@NotNull，@Pattern，他们位于 javax.validation.constraints 包下，只提供规范不提供实现。
  
> 而 hibernate validation 是对这个规范的实践（不要将 hibernate 和数据库 orm 框架联系在一起），他提供了相应的实现，并增加了一些其他校验注解，如 @Length，@Range 等等，他们位于 org.hibernate.validator.constraints 包下。
  
> 而万能的 spring 为了给开发者提供便捷，对 hibernate validation 进行了二次封装，显示校验 validated bean 时，你可以使用 spring validation 或者 hibernate validation，而 spring validation 另一个特性，便是其在 springmvc 模块中添加了自动校验，并将校验信息封装进了特定的类中。这无疑便捷了我们的 web 开发

## JSR 常用校验注解
```table
注解	解释
@Null	被注释的元素必须为 null
@NotNull	被注释的元素必须不为 null，但可以为empty
@NotEmpty	不能为null，而且长度必须大于0
@NotBlank	只能作用在String上，不能为null，而且调用trim()后，长度必须大于0
@AssertTrue	被注释的元素必须为 true
@AssertFalse	被注释的元素必须为 false
@Min	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Pattern	被注释的元素必须符合指定的正则表达式
@Email	被注释的元素必须是电子邮箱地址
```

## ``@Validated`` VS ``@Valid``
入参对象声明前需要加上``@Valid``或``@Validated``注解以启用校验。

- ``@Valid``：标准 JSR-303 规范的标记型注解，用来标记验证属性和方法返回值，进行级联和递归校验。
- ``@Validated``：Spring 的注解，是标准JSR-303的一个变种（补充），提供了一个分组功能，可以在入参验证时，根据不同的分组采用不同的验证机制。
- 在 Controller 中校验方法参数时，使用``@Valid``和``@Validated``并无特殊差异（若不需要分组校验的话）
- ``@Validated``注解可以用于类级别，用于支持 Spring 进行方法级别的参数校验。``@Valid``可以用在属性级别约束，用来表示级联校验。
- ``@Validated``只能用在类、方法和参数上，而``@Valid``可用于方法、字段、构造器和参数上。