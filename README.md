Docker L1JTW Server
===

This repo is forked from [l1j-tw-99nets](http://l1j-tw-99nets.googlecode.com/svn/trunk/L1J-TW_3.50c/), now it can build with docker in rancher.

![](http://i.giphy.com/l4HnNobFGTXXI56ik.gif)

## How To Build

**Create MYSQL DB**

I'm using mysql:5.6 for habit, so you can try other db images. Map container port 3306 to host public port 3306, be sure to keep it the same, because the server side is set its db port to 3306. Then don't forget to set root password.

```
# Port Map
0.0.0.0:3306->3306/tcp

# Environment Vars
MYSQ_ROOT_PASSWORD=[your-password]
```

![](http://i.imgur.com/ffA1rLJ.png)

**Create L1JTW Server**

Pull image, map port, link service and add required environment vars.

```
# From Image
tonypai/docker-l1jtw-server

# Port Map
0.0.0.0:2000->2000/tcp

# Service Link
db as db(optional)

# Environment Vars
DB_HOST=db         # Host name must equal to the 'as name' of service link
DB_PWD=[your-password]
RATE=10            # EXP, lawful, karma, adena drop and items drop rate
ENCHANT_CHANCE=40  # Weapon, armor and attr enchant chance
```

![](http://i.imgur.com/WNa56qg.png)

**Stack Graph**

After you finish these steps, it should perfectly running on your server.

![](http://i.imgur.com/AeNrzil.png)
