
## Методы для всех страниц

##### Получение информации текущего пользователя, в том числе статус по аутентификации
/api/v1/auth/current-user/:get

## Аноним

#### Регистрация

##### Регистрация пользователя. При отправке запроса, пользователю должно прийти письмо с паролем
/api/v1/auth/register/:post

##### Активация пользователя по данным ссылки из письма, полученного при вводе регистрационных данных
/api/v1/auth/activate/:post

#### Аутентификация

##### Аутентификация пользователя. Получение токена доступа и токена обновления
/api/v1/auth/login/:post

##### Обновление токена пользователя
/api/v1/auth/refresh-token/:post

#### Восстановление пароля

##### Восстановление пароля. В результате пользователю отправляется email со ссылкой на сброс пароля
/api/v1/auth/password-recovery/:post

##### Сброс пароля, по ссылке, пришедшей из письма
/api/v1/auth/reset-password/:post

#### Главная страница (перечень аукционов)

##### Список торгов
/api/v1/auctions/:get

Ввиду наличия постраничной навигации для каждого статуса аукциона, необходимо на странице совершить три запроса отдельно по каждому из статусов, для 
отрисовки их на главной странице.

#### Детальная страница аукциона

##### Детальная информация по аукциону
/api/v1/auctions/{guid}/:get

Для получения списка лотов с постраничной навигацией, воспользоваться отдельным методом

##### Перечень участников, которые участвуют в конкретных торгах
/api/v1/auctions/{guid}/participants/:get

##### Перечень лотов выставленных на торги (аукцион)
/api/v1/auctions/{guid}/lots/:get

#### Детальная страница лота

##### Получение лота по идентификатору
/api/v1/lots/{guid}/:get

##### История ставок по лоту и информация по текущим торгам
/api/v1/auctions/{auctionId}/lots/{lotId}/bids/:get

## Участник аукциона

#### Профиль

##### Получить данные профиля
/api/v1/auth/current-user/:get

##### Обновить данные профиля
/api/v1/auth/current-user/:put

#### Главная страница (перечень аукционов)

##### Список торгов
/api/v1/auctions/:get

Ввиду наличия постраничной навигации для каждого статуса аукциона, необходимо на странице совершить три запроса отдельно по каждому из статусов, для
отрисовки их на главной странице.

##### Перечень лотов для выставления лота на торги
/api/v1/lots/:get

##### Добавление нового лота
/api/v1/lots/:post

##### Подача заявки на торги, дополнительно можно выставить лоты (лоты не обязательны)
/api/v1/auctions/{guid}/participants/:post

##### Баланс личного счета можно получить
/api/v1/auth/current-user/:get

#### Детальная аукциона

##### Детальная информация по аукциону
/api/v1/auctions/{guid}/:get

Для получения списка лотов с постраничной навигацией, воспользоваться отдельным методом

##### Перечень участников, которые участвуют в конкретных торгах
/api/v1/auctions/{guid}/participants/:get

##### Перечень лотов выставленных на торги (аукцион)
/api/v1/auctions/{guid}/lots/:get

##### Подача заявки на торги, дополнительно можно выставить лоты (лоты не обязательны)
/api/v1/auctions/{guid}/participants/:post

#### Мои аукционы

##### Список торгов
/api/v1/auctions/:get

##### Выставить лот (допускается несколько последовательных запросов)
/api/v1/auctions/{guid}/participants/:post

#### Детальная страница лота торгующегося в данный момент

##### Получение лота по идентификатору
/api/v1/lots/{guid}/:get

##### История ставок по лоту и информация по текущим торгам
/api/v1/auctions/{auctionId}/lots/{lotId}/bids/:get

##### Совершение ставки участником
/api/v1/auctions/{auctionId}/lots/{lotId}/bids/:post

#### Детальная страница собственного лота с возможностью редактирования

##### Получение лота по идентификатору
/api/v1/lots/{guid}/:get

##### Удаление собственного лота
/api/v1/lots/{guid}/:delete

##### Обновление данных собственного лота
/api/v1/lots/{guid}/:put

##### Получение перечня аукционов, на которые можно выставить лот на торги
/api/v1/auctions/:get

Применять с фильтром по статусу

##### Подача заявки на торги
/api/v1/auctions/{guid}/participants/:post

#### Мои заявки

##### Список заявок. Для участника будут собственные
/api/v1/auctions/participate-requests/:get

#### Мои лоты

##### Список лотов. Для участника будут собственные лоты
/api/v1/lots/:get

##### Добавление лота
/api/v1/lots/:post

##### Удаление собственного лота
/api/v1/lots/:delete

#### Мои покупки

##### Список купленных лотов
/api/v1/participant/{guid}/purchases:get

#### Мой личный счет

##### Детальная информация личного счета
/api/v1/participants/{guid}/account/:get

##### Список транзакций
/api/v1/participants/{guid}/account/transactions/:get

##### Пополнение личного счета
/api/v1/participants/{guid}/account/:put

##### Вывод средств со счета
/api/v1/participants/{guid}/account/withdraw/:post

## Администратор торгов

#### Профиль пользователя
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=263-3945&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Информация о пользователе совместно с ролью
/api/v1/auth/current-user/:get

##### Обновление информации совместно с ролью
/api/v1/auth/current-user/:put

#### Аукционы
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=235-3994&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Список аукционов
/api/v1/auctions/:get

##### Удаление аукциона
/api/v1/auctions/{guid}/:delete

##### Добавление аукциона
/api/v1/auctions/:post

#### Детальная вновь созданного аукциона (подготовка к торгам)
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=304-5377&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Базовая информация по аукциону
/api/v1/auctions/{guid}/:get

##### Перечень лотов аукциона
/api/v1/auctions/{guid}/lots/:get

##### Перечень участников
/api/v1/auctions/{guid}/participants/:get

##### Удаление участника аукциона
/api/v1/auctions/{guid}/participants/{participantGuid}/:delete

##### Заявки на участие по аукциону
/api/v1/auctions/{guid}/participants/participate-application/:get

##### Отклонение заявки на участие
/api/v1/auctions/{guid}/participants/participate-application/{action}/:post

##### Принятие заявки на участие
/api/v1/auctions/{guid}/participants/participate-application/{action}/:post

##### Запуск торгов
/api/v1/auctions/{guid}/{action}/:post

##### Изменение даты начала торгов
/api/v1/auctions/{guid}/:patch

##### Удаление аукциона
/api/v1/auctions/{guid}/:delete

#### Детальная активного (торгующегося) аукциона
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=322-1627&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Краткая информация по аукциону
/api/v1/auctions/{guid}/:get

##### Завершение торгов по аукциону
/api/v1/auctions/{guid}/{action}/:post

##### Список лотов на торгах
/api/v1/auctions/{guid}/lots/:get

##### Лоты в ожидании с постраничной навигацией
/api/v1/auctions/{guid}/lots/:get

##### Запуск торгов по лотам, которые находятся в ожидании
/api/v1/auctions/{guid}/lots/{lotGuid}/for-sale/:post

##### Удаление лота с торгов, который находится в ожидании торгов по лоту
/api/v1/auctions/{guid}/lots/{lotGuid}/:delete

##### Перечень проданных лотов с победителем
/api/v1/auctions/{guid}/lots/:get

##### Перечень участников аукциона с продаваемыми и приобретенными лотами
/api/v1/auctions/{guid}/participants/:get

##### Перечень заявок на участие, допускается подавать даже для активного аукцона
/api/v1/auctions/{guid}/participants/participate-application/:get

##### Принятие заявки на участие
/api/v1/auctions/{guid}/participants/participate-application/accept/:post

##### Отклонение заявки на участие
/api/v1/auctions/{guid}/participants/participate-application/decline/:post

##### Блокировка участника аукциона, он не может назначать ставки
/api/v1/auctions/{guid}/participants/:delete

#### Детальная страница аукциона, по которому завершены торги
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=347-7808&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Краткая информация по аукциону
/api/v1/auctions/{guid}/:get

##### Перечень лотов аукциона с победителями
/api/v1/auctions/{guid}/lots/:get

##### Перечень участников аукциона с постраничной навигацией с продаваемыми и приобретенными лотами
/api/v1/auctions/{guid}/participants/:get

#### Детальная страница вновь созданного лота (Страница редактирования)
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=482-7385&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Получение информации о лоте с историей изменений
/api/v1/lots/{guid}/:get

##### Изменение изображения
/api/v1/file/:post
/api/v1/lots/{guid}/:put

##### Изменение параметров лота
/api/v1/lots/{guid}/:put

##### Удаление лота
/api/v1/lots/{guid}/:delete

#### Детальная страница торгующегося (заявленного на торги) лота
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=583-9551&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Информация о лоте с историей изменений
/api/v1/lots/{guid}/:get

##### История ставок торгующегося лота
/api/v1/auctions/{auctionId}/lots/{lotId}/bids/:get

##### Информация по последней принятой ставке
/api/v1/auctions/{auctionId}/lots/{lotId}/bids/:get

#### Детальная страница лота перешедшего во владение
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=583-9339&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Информация о лоте с историей изменений
/api/v1/lots/{guid}/:get

##### Сохранение информации о лоте (без изображения)
/api/v1/lots/{guid}/:put

#### Детальная страница участника торгов
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=429-2990&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Информация об участнике торгов с номером и ФИО, Балансе, выставленных лотах 
/api/v1/auctions/{guid}/participants/{participantGuid}/:get

##### Удаление участника с торгов
/api/v1/auctions/{guid}/participants/{participantGuid}/:delete

#### Заявки на участие в аукционе
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=235-4335&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Список заявок по всем аукционам во всех статусах
/api/v1/auctions/participants/participate-application/:get

##### Принятие заявки на аукцион
/api/v1/auctions/{guid}/participants/participate-application/accept/:post

##### Отклонение заявки
/api/v1/auctions/{guid}/participants/participate-application/decline/:post

#### Пользователи
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=235-4446&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Список пользователей
/api/v1/users/:get

##### Добавление пользователя
/api/v1/users/:post

##### Удаление пользователя
/api/v1/users/{id}/:post

#### Детальная пользователя
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=446-7862&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Получение краткой информации
/api/v1/users/{guid}/:get

##### Сохранение имени, роли
/api/v1/users/{guid}/:put

##### Удаление профиля пользователя
/api/v1/users/{guid}/:delete

#### Отчет по участнику
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=450-7981&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Перечень аукционов, в котором пользователь является участником
/api/v1/auctions/:get

##### Перечень заявок на участие пользователем
С фильтром по текущему участнику

/api/v1/auctions/participants/participate-application/:get

##### Перечень лотов пользователя
С фильтром по текущему участнику

/api/v1/lots/:get

##### Перечень приобретенных лотов пользователя
С фильтром по текущему участнику

/api/v1/participant/purchases:get

##### Личный счет пользователя
/api/v1/participants/{guid}/account/:get

##### Перечень транзакций личного счета пользователя
/api/v1/participants/{guid}/account/transactions/:get

#### Лоты
[Прототип](https://www.figma.com/proto/kC51lgTFhqiRo7dH2oZIUv/OnlineAuction?page-id=6%3A22&node-id=235-4551&node-type=canvas&viewport=383%2C-622%2C0.5&t=NvyZYhQXvJoGTjiS-1&scaling=scale-down-width&content-scaling=fixed&starting-point-node-id=6%3A23&hide-ui=1)

##### Полный список лотов
/api/v1/lots/:get

##### Добавление лота с указанием владельца
/api/v1/lots/:post

##### Получение информации о лоте
/api/v1/lots/{guid}/:get

##### Изменение изображения
/api/v1/file/:post

/api/v1/lots/{guid}/:put

##### Изменение параметров лота
/api/v1/lots/{guid}/:put

##### Удаление лота
/api/v1/lots/{guid}/:delete